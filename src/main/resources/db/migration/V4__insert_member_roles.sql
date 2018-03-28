DO $$
-- privileges
DECLARE close_project_id bigint;
DECLARE show_members_id bigint;
DECLARE manage_members_id bigint;
DECLARE show_tags_id bigint;
DECLARE manage_tags_id bigint;
DECLARE show_tasks_id bigint;
DECLARE manage_tasks_id bigint;
DECLARE executor_id bigint;
-- roles
DECLARE project_manager_id bigint;
DECLARE project_admin_id bigint;
DECLARE tasks_manager_id bigint;
DECLARE executor_role_id bigint;
BEGIN
  INSERT INTO member_privileges (name, code) VALUES ('Закрытие проекта', 'CLOSE_PROJECT') RETURNING id INTO close_project_id;
  INSERT INTO member_privileges (name, code) VALUES ('Просмотр участников', 'SHOW_MEMBERS') RETURNING id INTO show_members_id;
  INSERT INTO member_privileges (name, code) VALUES ('Управление участниками', 'MANAGE_MEMBERS') RETURNING id INTO manage_members_id;
  INSERT INTO member_privileges (name, code) VALUES ('Просмотр тегов', 'SHOW_TAGS') RETURNING id INTO show_tags_id;
  INSERT INTO member_privileges (name, code) VALUES ('Управление тегами', 'MANAGE_TAGS') RETURNING id INTO manage_tags_id;
  INSERT INTO member_privileges (name, code) VALUES ('Просмотр задач', 'SHOW_TASKS') RETURNING id INTO show_tasks_id;
  INSERT INTO member_privileges (name, code) VALUES ('Управление задачами', 'MANAGE_TASKS') RETURNING id INTO manage_tasks_id;
  INSERT INTO member_privileges (name, code) VALUES ('Исполнитель', 'EXECUTOR') RETURNING id INTO executor_id;

  INSERT INTO member_roles (name, code) VALUES ('Менеджер проекта', 'PROJECT_MANAGER') RETURNING id INTO project_manager_id;
  INSERT INTO member_roles (name, code) VALUES ('Администратор проекта', 'PROJECT_ADMIN') RETURNING id INTO project_admin_id;
  INSERT INTO member_roles (name, code) VALUES ('Менеджер задач', 'PROJECT_TASK_MANAGER') RETURNING id INTO tasks_manager_id;
  INSERT INTO member_roles (name, code) VALUES ('Исполнитель проекта', 'PROJECT_EXECUTOR') RETURNING id INTO executor_role_id;

  INSERT INTO member_role_privilege (member_role_id, member_privilege_id) VALUES
    (project_manager_id, close_project_id),
    (project_manager_id, show_members_id),
    (project_manager_id, manage_members_id),
    (project_manager_id, show_tags_id),
    (project_manager_id, manage_tags_id),
    (project_manager_id, show_tasks_id),
    (project_manager_id, manage_tasks_id),
    (project_manager_id, executor_id);

  INSERT INTO member_role_privilege (member_role_id, member_privilege_id) VALUES
    (project_admin_id, show_members_id),
    (project_admin_id, manage_members_id),
    (project_admin_id, show_tasks_id),
    (project_admin_id, executor_id);

  INSERT INTO member_role_privilege (member_role_id, member_privilege_id) VALUES
    (tasks_manager_id, show_members_id),
    (tasks_manager_id, show_tasks_id),
    (tasks_manager_id, manage_tags_id),
    (tasks_manager_id, show_tasks_id),
    (tasks_manager_id, manage_tasks_id),
    (tasks_manager_id, executor_id);

  INSERT INTO member_role_privilege (member_role_id, member_privilege_id) VALUES
    (executor_role_id, show_members_id),
    (executor_role_id, show_tags_id),
    (executor_role_id, show_tasks_id),
    (executor_role_id, executor_id);
END $$;
