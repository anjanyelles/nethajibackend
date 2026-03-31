-- Create admin user for production Render database
-- Password: admin123
-- Email: admin@nethaji.com

-- Delete existing admin user if exists
DELETE FROM users WHERE email = 'admin@nethaji.com';

-- Insert admin user with hashed password
-- Salt: f21d744f-2f8e-4b93-866d-a985879dbd807f1c1013-4
-- Password Hash for "admin123" with above salt
INSERT INTO users (
    id, 
    email, 
    first_name, 
    last_name, 
    mobile_number, 
    password_hash, 
    salt, 
    user_type, 
    is_active, 
    created_at, 
    updated_at
) VALUES (
    gen_random_uuid(),
    'admin@nethaji.com',
    'Admin',
    'User',
    '9999999999',
    'BEA5814087D7CCE9868500543A43DE166E1D51F91E120E8E4A66E69E2F166C450D84F7FB27FB7C903A5D350CCB65D1700F4F47B57DCB9F113BFE0A83913A33D9',
    'f21d744f-2f8e-4b93-866d-a985879dbd807f1c1013-4',
    'ADMIN',
    true,
    NOW(),
    NOW()
);

-- Verify admin user was created
SELECT id, email, first_name, last_name, user_type, is_active, created_at 
FROM users 
WHERE email = 'admin@nethaji.com';
