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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.sonar.db.DbTester;
import org.sonar.db.organization.OrganizationDto;
import org.sonar.db.user.GroupDto;
import org.sonar.db.user.GroupMembershipDto;
import org.sonar.db.user.GroupMembershipQuery;
import org.sonar.db.user.UserDto;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.sonar.db.user.GroupMembershipQuery.IN;

public class OrganizationMembershipUpdaterTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Rule
  public final DbTester db = DbTester.create();

  private OrganizationMembershipUpdater underTest = new OrganizationMembershipUpdater(db.getDbClient());

  @Test
  public void add_member_adds_user_as_member_of_organization() throws Exception {
    OrganizationDto organization = db.organizations().insert();
    db.users().insertDefaultGroup(organization, "default");
    // other organization
    db.organizations().insert();
    UserDto user = db.users().insertUser();

    underTest.addMember(db.getSession(), organization, user);

    assertOrganizationMembership(organization, user);
  }

  @Test
  public void add_member_adds_user_as_member_of_default_group() throws Exception {
    OrganizationDto organization = db.organizations().insert();
    GroupDto defaultGroup = db.users().insertDefaultGroup(organization, "default");
    db.users().insertGroup(organization, "other");
    OrganizationDto otherOrganization = db.organizations().insert();
    db.users().insertDefaultGroup(otherOrganization, "default");
    UserDto user = db.users().insertUser();

    underTest.addMember(db.getSession(), organization, user);

    assertDefaultGroupMembership(organization, defaultGroup, user);
  }

  @Test
  public void does_not_fail_to_add_member_when_user_is_already_member_of_organization() throws Exception {
    OrganizationDto organization = db.organizations().insert();
    db.users().insertDefaultGroup(organization, "default");
    UserDto user = db.users().insertUser();
    db.organizations().addMember(organization, user);

    underTest.addMember(db.getSession(), organization, user);

    assertOrganizationMembership(organization, user);
  }

  @Test
  public void fail_when_organization_has_no_default_group() throws Exception {
    OrganizationDto organization = db.organizations().insert();
    UserDto user = db.users().insertUser();

    expectedException.expect(IllegalStateException.class);
    expectedException.expectMessage(format("Default group doesn't exist on default organization '%s'", organization.getKey()));

    underTest.addMember(db.getSession(), organization, user);
  }

  private void assertOrganizationMembership(OrganizationDto organization, UserDto user) {
    assertThat(db.getDbClient().organizationMemberDao().select(db.getSession(), organization.getUuid(), user.getId())).isPresent();
  }

  private void assertDefaultGroupMembership(OrganizationDto organization, GroupDto defaultGroup, UserDto user) {
    assertThat(db.getDbClient().groupMembershipDao().selectGroups(db.getSession(), GroupMembershipQuery.builder()
      .membership(IN)
      .organizationUuid(organization.getUuid()).build(),
      user.getId(), 0, 10))
        .extracting(GroupMembershipDto::getId)
        .containsOnly(defaultGroup.getId().longValue());
  }
}
