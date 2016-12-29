/*
 * SonarQube
 * Copyright (C) 2009-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
// @flow
import { getJSON } from '../helpers/request';

export function getFacets (query: {}, facets: Array<string>): Promise<*> {
  const url = '/api/issues/search';
  const data = {
    ...query,
    facets: facets.join(),
    ps: 1,
    additionalFields: '_all'
  };
  return getJSON(url, data).then(r => {
    return { facets: r.facets, response: r };
  });
}

export function getFacet (query: {}, facet: string): Promise<*> {
  return getFacets(query, [facet]).then(r => {
    return { facet: r.facets[0].values, response: r.response };
  });
}

export function getSeverities (query: {}): Promise<*> {
  return getFacet(query, 'severities').then(r => r.facet);
}

export function getTags (query: {}): Promise<*> {
  return getFacet(query, 'tags').then(r => r.facet);
}

export function extractAssignees (
    facet: Array<{ val: string }>,
    response: { users: Array<{ login: string }> }
) {
  return facet.map(item => {
    const user = response.users.find(user => user.login = item.val);
    return { ...item, user };
  });
}

export function getAssignees (query: {}): Promise<*> {
  return getFacet(query, 'assignees').then(r => extractAssignees(r.facet, r.response));
}

export function getIssuesCount (query: {}): Promise<*> {
  const url = '/api/issues/search';
  const data = { ...query, ps: 1, facetMode: 'effort' };
  return getJSON(url, data).then(r => {
    return { issues: r.total, debt: r.debtTotal };
  });
}
