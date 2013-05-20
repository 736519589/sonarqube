/*
 * SonarQube, open source software quality management tool.
 * Copyright (C) 2008-2013 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * SonarQube is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * SonarQube is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.sonar.plugins.core.issue;

import org.junit.Test;
import org.sonar.api.resources.Project;
import org.sonar.core.issue.db.IssueDao;
import org.sonar.core.issue.db.IssueDto;

import java.util.Date;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class InitialOpenIssuesSensorTest {

  InitialOpenIssuesStack stack = mock(InitialOpenIssuesStack.class);
  IssueDao issueDao = mock(IssueDao.class);
  InitialOpenIssuesSensor sensor = new InitialOpenIssuesSensor(stack, issueDao);


  @Test
  public void should_select_module_open_issues() {
    Project project = new Project("key");
    project.setId(1);
    sensor.analyse(project, null);

    verify(issueDao).selectNonClosedIssuesByRootComponent(1);
    verify(stack).setIssues(anyListOf(IssueDto.class), any(Date.class));
  }
}
