-- Check if image_url column exists and add if not
-- Run this in MySQL Workbench or command line

-- First, check current structure
DESCRIBE book;

-- If image_url column doesn't exist, run this:
ALTER TABLE book ADD COLUMN IF NOT EXISTS image_url VARCHAR(255);

-- Verify the column was added
DESCRIBE book;

-- Optional: Check current data
SELECT id, title, image_url FROM book LIMIT 5;
