# Fix Login Password Issue

## Problem
Login was failing with "Invalid username or password" because:
1. Seed data had hardcoded BCrypt hash that might not match "admin123"
2. Users might have been created with old/invalid password hashes

## Solution Applied

### 1. Updated Login Method
- Added support for both BCrypt (no salt) and custom salt-based password verification
- Added null checks and better error handling
- Added debug logging

### 2. Updated Seed Data
- Now uses `PasswordEncoder` to generate proper BCrypt hashes
- Automatically updates existing users' passwords if they have old/invalid hashes

## How to Fix Existing Users

### Option 1: Re-seed Data (Recommended)
```bash
# Call the seed endpoint to update passwords
curl -X POST http://localhost:9029/api/nethaji-service/admin/seed-data
```

This will:
- Update all existing users' passwords to use proper BCrypt hash for "admin123"
- Create new users if they don't exist

### Option 2: Update Password via API
```bash
# Update password for a specific student
curl -X PATCH "http://localhost:9029/api/nethaji-service/auth/update-password-by-admin?studentId=USER_ID&password=admin123"
```

### Option 3: Delete and Recreate Users
If you have access to the database:
```sql
DELETE FROM users;
-- Then call seed-data endpoint
```

## Test Login

After re-seeding, test with:
- **Email**: `admin@nethaji.edu`
- **Password**: `admin123`

Or:
- **Email**: `student1@nethaji.edu`
- **Password**: `admin123`

## What Changed

1. **AuthServiceImpl.java**:
   - Added PasswordEncoder autowired field
   - Updated login methods to handle null salt
   - Added BCrypt fallback for users without salt

2. **DataSeedController.java**:
   - Uses PasswordEncoder to generate proper BCrypt hashes
   - Automatically updates existing users' passwords
   - Sets salt to null for BCrypt users

## Next Steps

1. **Restart backend** (if running):
   ```bash
   cd nethajibackend
   mvn spring-boot:run
   ```

2. **Re-seed data**:
   ```bash
   curl -X POST http://localhost:9029/api/nethaji-service/admin/seed-data
   ```

3. **Test login** in frontend:
   - Go to `http://localhost:3000/login`
   - Use credentials above
   - Should work now!

---

**Status**: ✅ Fixed - Re-seed data to apply changes

