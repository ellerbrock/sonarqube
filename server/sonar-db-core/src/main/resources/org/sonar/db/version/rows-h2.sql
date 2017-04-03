-- All the rows inserted during Rails migrations. Rows inserted during server startup tasks (Java) are excluded : rules, profiles, metrics, ...

INSERT INTO USERS(ID, LOGIN, NAME, EMAIL, EXTERNAL_IDENTITY, EXTERNAL_IDENTITY_PROVIDER, USER_LOCAL, CRYPTED_PASSWORD, SALT, IS_ROOT, CREATED_AT, UPDATED_AT) VALUES (1, 'admin', 'Administrator', '', 'admin', 'sonarqube', true, 'a373a0e667abb2604c1fd571eb4ad47fe8cc0878', '48bc4b0d93179b5103fd3885ea9119498e9d161b', false, '1418215735482', '1418215735482');
ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 2;

INSERT INTO ORGANIZATIONS (UUID, KEE, NAME, GUARDED, CREATED_AT, UPDATED_AT) VALUES ('AVdqnciQUUs7Zd3KPvFD', 'default-organization', 'Default Organization', true, '1474962596482', '1474962596482');
INSERT INTO INTERNAL_PROPERTIES (KEE, IS_EMPTY, TEXT_VALUE, CREATED_AT) VALUES ('organization.default', false, 'AVdqnciQUUs7Zd3KPvFD', '1474962596482');

INSERT INTO GROUPS(ID, ORGANIZATION_UUID, NAME, DESCRIPTION, CREATED_AT, UPDATED_AT) VALUES (1, 'AVdqnciQUUs7Zd3KPvFD', 'sonar-administrators', 'System administrators', '2011-09-26 22:27:51.0', '2011-09-26 22:27:51.0');
INSERT INTO GROUPS(ID, ORGANIZATION_UUID, NAME, DESCRIPTION, CREATED_AT, UPDATED_AT) VALUES (2, 'AVdqnciQUUs7Zd3KPvFD', 'sonar-users', 'Any new users created will automatically join this group', '2011-09-26 22:27:51.0', '2011-09-26 22:27:51.0');
ALTER TABLE GROUPS ALTER COLUMN ID RESTART WITH 3;

INSERT INTO GROUP_ROLES(ID, ORGANIZATION_UUID, GROUP_ID, RESOURCE_ID, ROLE) VALUES (1, 'AVdqnciQUUs7Zd3KPvFD', 1, null, 'admin');
INSERT INTO GROUP_ROLES(ID, ORGANIZATION_UUID, GROUP_ID, RESOURCE_ID, ROLE) VALUES (2, 'AVdqnciQUUs7Zd3KPvFD', 1, null, 'profileadmin');
INSERT INTO GROUP_ROLES(ID, ORGANIZATION_UUID, GROUP_ID, RESOURCE_ID, ROLE) VALUES (3, 'AVdqnciQUUs7Zd3KPvFD', 1, null, 'gateadmin');
INSERT INTO GROUP_ROLES(ID, ORGANIZATION_UUID, GROUP_ID, RESOURCE_ID, ROLE) VALUES (4, 'AVdqnciQUUs7Zd3KPvFD', null, null, 'scan');
INSERT INTO GROUP_ROLES(ID, ORGANIZATION_UUID, GROUP_ID, RESOURCE_ID, ROLE) VALUES (5, 'AVdqnciQUUs7Zd3KPvFD', null, null, 'provisioning');
INSERT INTO GROUP_ROLES(ID, ORGANIZATION_UUID, GROUP_ID, RESOURCE_ID, ROLE) VALUES (6, 'AVdqnciQUUs7Zd3KPvFD', 1, null, 'provisioning');
ALTER TABLE GROUP_ROLES ALTER COLUMN ID RESTART WITH 7;

INSERT INTO GROUPS_USERS(USER_ID, GROUP_ID) VALUES (1, 1);
INSERT INTO GROUPS_USERS(USER_ID, GROUP_ID) VALUES (1, 2);

INSERT INTO ORGANIZATION_MEMBERS(ORGANIZATION_UUID, USER_ID) VALUES ('AVdqnciQUUs7Zd3KPvFD', 1);
