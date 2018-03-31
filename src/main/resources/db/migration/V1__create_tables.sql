-- account_roles table
CREATE TABLE account_roles (
  id BIGSERIAL PRIMARY KEY NOT NULL,
  name VARCHAR(20) NOT NULL,
  code VARCHAR(20) NOT NULL
);
CREATE UNIQUE INDEX account_roles_code_uindex ON account_roles (code);
CREATE UNIQUE INDEX account_roles_name_uindex ON account_roles (name);

-- accounts table
CREATE TABLE accounts (
  id BIGSERIAL PRIMARY KEY NOT NULL,
  login VARCHAR(50) NOT NULL,
  password VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  full_name VARCHAR(255) NOT NULL,
  blocked BOOLEAN DEFAULT FALSE,
  account_role_id BIGINT,
  CONSTRAINT accounts_account_roles_id_fk FOREIGN KEY (account_role_id) REFERENCES account_roles (id)
);
CREATE UNIQUE INDEX accounts_login_uindex ON accounts (login);
CREATE UNIQUE INDEX accounts_email_uindex ON accounts (email);

-- member_privileges table
CREATE TABLE member_privileges (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    code VARCHAR(20) NOT NULL,
    name VARCHAR(80) NOT NULL
);
CREATE UNIQUE INDEX member_privileges_name_uindex ON member_privileges (name);
CREATE UNIQUE INDEX member_privileges_code_uindex ON member_privileges (code);

-- member_roles table
CREATE TABLE member_roles (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(80) NOT NULL,
    code VARCHAR(20) NOT NULL
);
CREATE UNIQUE INDEX member_roles_name_uindex ON member_roles (name);
CREATE UNIQUE INDEX member_roles_code_uindex ON member_roles (code);

-- member_role_privilege table
CREATE TABLE member_role_privilege (
  id BIGSERIAL PRIMARY KEY NOT NULL,
  member_role_id BIGINT NOT NULL,
  member_privilege_id BIGINT NOT NULL,
  CONSTRAINT member_role_privilege_member_roles_id_fk FOREIGN KEY (member_role_id) REFERENCES member_roles (id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT member_role_privilege_member_privileges_id_fk FOREIGN KEY (member_privilege_id) REFERENCES member_privileges (id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- projects table
CREATE TABLE projects (
  id BIGSERIAL PRIMARY KEY NOT NULL,
  name VARCHAR(120) NOT NULL,
  description TEXT,
  create_date DATE NOT NULL,
  opened BOOLEAN DEFAULT TRUE  NOT NULL,
  account_author_id BIGINT NOT NULL,
  CONSTRAINT projects_accounts_id_fk FOREIGN KEY (account_author_id) REFERENCES accounts (id) ON UPDATE CASCADE
);
CREATE UNIQUE INDEX projects_name_uindex ON projects (name);

-- members table
CREATE TABLE members (
  id BIGSERIAL PRIMARY KEY NOT NULL,
  project_id BIGINT NOT NULL,
  account_id BIGINT NOT NULL,
  member_role_id BIGINT NOT NULL,
  CONSTRAINT members_projects_id_fk FOREIGN KEY (project_id) REFERENCES projects (id),
  CONSTRAINT members_accounts_id_fk FOREIGN KEY (account_id) REFERENCES accounts (id),
  CONSTRAINT members_member_roles_id_fk FOREIGN KEY (member_role_id) REFERENCES member_roles (id)
);

-- task_statuses table
CREATE TABLE task_statuses (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(20) NOT NULL,
    code VARCHAR(20) NOT NULL
);
CREATE UNIQUE INDEX task_statuses_name_uindex ON task_statuses (name);
CREATE UNIQUE INDEX task_statuses_code_uindex ON task_statuses (code);

-- tasks table
CREATE TABLE tasks (
  id BIGSERIAL PRIMARY KEY NOT NULL,
  name VARCHAR(120) NOT NULL,
  description TEXT,
  task_state_id BIGINT NOT NULL,
  member_author_id BIGINT NOT NULL,
  member_assignee_id BIGINT NOT NULL,
  project_id BIGINT NOT NULL,
  CONSTRAINT tasks_task_statuses_id_fk FOREIGN KEY (task_state_id) REFERENCES task_statuses (id) ON UPDATE CASCADE,
  CONSTRAINT tasks_accounts_author_id_fk FOREIGN KEY (member_author_id) REFERENCES members (id) ON UPDATE CASCADE,
  CONSTRAINT tasks_accounts_assignee_id_fk FOREIGN KEY (member_assignee_id) REFERENCES members (id) ON UPDATE CASCADE,
  CONSTRAINT tasks_project_id_fk FOREIGN KEY (project_id) REFERENCES projects (id) ON UPDATE CASCADE ON DELETE CASCADE
);
CREATE INDEX tasks_name_index ON tasks (name);

-- tags table
CREATE TABLE tags (
  id BIGSERIAL PRIMARY KEY NOT NULL,
  name VARCHAR(120) NOT NULL,
  color VARCHAR(6) NOT NULL,
  project_id BIGINT NOT NULL,
  CONSTRAINT tags_projects_id_fk FOREIGN KEY (project_id) REFERENCES projects (id) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE INDEX tags_name_index ON tags (name);

-- task_tag table
CREATE TABLE task_tag (
  id BIGSERIAL PRIMARY KEY NOT NULL,
  task_id BIGINT NOT NULL,
  tag_id BIGINT NOT NULL,
  CONSTRAINT task_tag_tasks_id_fk FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT task_tag_tags_id_fk FOREIGN KEY (tag_id) REFERENCES tags (id)
);

-- task_steps table
CREATE TABLE task_steps (
  id BIGSERIAL NOT NULL,
  name VARCHAR(120) NOT NULL,
  completed BOOLEAN DEFAULT FALSE  NOT NULL,
  need_report BOOLEAN DEFAULT FALSE NOT NULL,
  report TEXT,
  task_id BIGINT NOT NULL,
  CONSTRAINT task_steps_tasks_id_fk FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE INDEX task_steps_name_index ON task_steps (name);

-- criteria table
CREATE TABLE criteria (
  id BIGSERIAL PRIMARY KEY NOT NULL,
  name VARCHAR(120) NOT NULL,
  expected_value INT NOT NULL,
  actual_value INT,
  task_id BIGINT NOT NULL,
  CONSTRAINT criteria_tasks_id_fk FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE INDEX criteria_name_index ON criteria (name);

-- event_types table
CREATE TABLE event_types (
  id BIGSERIAL PRIMARY KEY NOT NULL,
  code VARCHAR(10) NOT NULL
);
CREATE UNIQUE INDEX event_types_code_uindex ON event_types (code);

-- task_events table
CREATE TABLE task_events (
  id BIGSERIAL PRIMARY KEY NOT NULL,
  name VARCHAR(200) NOT NULL,
  event_time TIMESTAMP NOT NULL,
  event_type_id BIGINT NOT NULL,
  task_id BIGINT NOT NULL,
  member_id BIGINT NOT NULL,
  CONSTRAINT task_events_event_type_id_fk FOREIGN KEY (event_type_id) REFERENCES event_types (id),
  CONSTRAINT task_events_tasks_id_fk FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT task_events_members_id_fk FOREIGN KEY (member_id) REFERENCES members (id) ON DELETE SET NULL ON UPDATE CASCADE
);
