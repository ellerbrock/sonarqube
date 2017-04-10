/*
 * SonarQube
 * Copyright (C) 2009-2017 SonarSource SA
 * mailto:info AT sonarsource DOT com
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

package org.sonar.server.organization;

import org.sonar.db.DbClient;
import org.sonar.db.DbSession;
import org.sonar.db.organization.OrganizationDto;
import org.sonar.db.organization.OrganizationMemberDto;
import org.sonar.db.user.UserDto;
import org.sonar.db.user.UserGroupDto;

public class OrganizationMembershipUpdater {

  private final DbClient dbClient;

  public OrganizationMembershipUpdater(DbClient dbClient) {
    this.dbClient = dbClient;
  }

  public void addMember(DbSession dbSession, OrganizationDto organizationDto, UserDto userDto) {
    if (isMemberOf(dbSession, organizationDto, userDto)) {
      return;
    }
    dbClient.organizationMemberDao().insert(dbSession, new OrganizationMemberDto()
      .setOrganizationUuid(organizationDto.getUuid())
      .setUserId(userDto.getId()));
    int defaultGroupId = getDefaultGroupId(dbSession, organizationDto);
    dbClient.userGroupDao().insert(dbSession, new UserGroupDto().setGroupId(defaultGroupId).setUserId(userDto.getId()));
  }

//  public void removeMember(DbSession dbSession, OrganizationDto organizationDto, UserDto userDto) {
//    if (!isMemberOf(dbSession, organizationDto, userDto)) {
//      return;
//    }
//    int defaultGroupId = getDefaultGroupId(dbSession, organizationDto);
//    dbClient.userGroupDao().delete(dbSession, defaultGroupId, userDto.getId());
//    dbClient.organizationMemberDao().delete(dbSession, organizationDto.getUuid(), userDto.getId());
//  }

  private boolean isMemberOf(DbSession dbSession, OrganizationDto organizationDto, UserDto userDto) {
    return dbClient.organizationMemberDao().select(dbSession, organizationDto.getUuid(), userDto.getId()).isPresent();
  }

  private int getDefaultGroupId(DbSession dbSession, OrganizationDto organizationDto) {
    return dbClient.organizationDao().getDefaultGroupId(dbSession, organizationDto.getUuid())
      .orElseThrow(() -> new IllegalStateException(String.format("Default group doesn't exist on default organization '%s'", organizationDto.getKey())));
  }
}
