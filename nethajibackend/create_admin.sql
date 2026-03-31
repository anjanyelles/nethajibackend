-- Create admin user with credentials: admin@nethaji.com / admin123
-- Salt for password hashing
-- Password will be hashed using SHA-512 with 100 iterations

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
    'E8B8F0F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3F3',
    'admin-salt-123',
    'ADMIN',
    true,
    NOW(),
    NOW()
)
ON CONFLICT (email) DO UPDATE SET
    password_hash = EXCLUDED.password_hash,
    salt = EXCLUDED.salt,
    updated_at = NOW();
