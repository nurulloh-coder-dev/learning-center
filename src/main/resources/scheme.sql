CREATE UNIQUE INDEX IF NOT EXISTS uk_user_org_role_active
    ON user_organizations (user_id, organization_id, role)
    WHERE deleted = false;