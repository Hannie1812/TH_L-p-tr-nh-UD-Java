# Book API Documentation

## Base URL
```
http://localhost:8080/api/v1
```

## Endpoints

### 1. Get All Books
**GET** `/books`

Lấy danh sách tất cả sách với phân trang và sắp xếp.

**Parameters:**
- `pageNo` (optional): Số trang (mặc định: 0)
- `pageSize` (optional): Kích thước trang (mặc định: 20)
- `sortBy` (optional): Trường sắp xếp (mặc định: "id")

**Response:**
```json
[
  {
    "id": 1,
    "title": "Spring Boot Guide",
    "author": "John Doe",
    "price": 29.99,
    "category": "Programming"
  }
]
```

### 2. Get Book by ID
**GET** `/books/id/{id}`

Lấy thông tin sách theo ID.

**Path Variable:**
- `id`: ID của sách

**Response:**
```json
{
  "id": 1,
  "title": "Spring Boot Guide",
  "author": "John Doe",
  "price": 29.99,
  "category": "Programming"
}
```

### 3. Create Book
**POST** `/books`

Tạo sách mới.

**Request Body:**
```json
{
  "title": "New Book Title",
  "author": "Author Name",
  "price": 19.99,
  "categoryId": 1
}
```

**Validation Rules:**
- `title`: Bắt buộc, độ dài 1-50 ký tự
- `author`: Bắt buộc, độ dài 1-50 ký tự
- `price`: Bắt buộc, phải > 0
- `categoryId`: Bắt buộc, phải tồn tại trong database

**Response:**
```json
{
  "id": 2,
  "title": "New Book Title",
  "author": "Author Name",
  "price": 19.99,
  "category": "Programming"
}
```

**Status Codes:**
- `201 Created`: Tạo thành công
- `400 Bad Request`: Dữ liệu không hợp lệ
- `404 Not Found`: Category không tồn tại

### 4. Update Book
**PUT** `/books/{id}`

Cập nhật thông tin sách.

**Path Variable:**
- `id`: ID của sách cần cập nhật

**Request Body:**
```json
{
  "title": "Updated Book Title",
  "author": "Updated Author Name",
  "price": 25.99,
  "categoryId": 2
}
```

**Response:**
```json
{
  "id": 1,
  "title": "Updated Book Title",
  "author": "Updated Author Name",
  "price": 25.99,
  "category": "Fiction"
}
```

**Status Codes:**
- `200 OK`: Cập nhật thành công
- `400 Bad Request`: Dữ liệu không hợp lệ
- `404 Not Found`: Book hoặc Category không tồn tại

### 5. Delete Book
**DELETE** `/books/{id}`

Xóa sách theo ID.

**Path Variable:**
- `id`: ID của sách cần xóa

**Status Codes:**
- `204 No Content`: Xóa thành công
- `404 Not Found`: Book không tồn tại

### 6. Search Books
**GET** `/books/search`

Tìm kiếm sách theo từ khóa.

**Parameters:**
- `keyword`: Từ khóa tìm kiếm (tìm trong title và author)

**Response:**
```json
[
  {
    "id": 1,
    "title": "Spring Boot Guide",
    "author": "John Doe",
    "price": 29.99,
    "category": "Programming"
  }
]
```

## Error Handling

### Validation Errors (400)
```json
{
  "timestamp": "2024-01-01T12:00:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Title must be between 1 and 50 characters",
  "path": "/api/v1/books"
}
```

### Not Found Errors (404)
```json
{
  "timestamp": "2024-01-01T12:00:00.000+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Category not found with id: 999",
  "path": "/api/v1/books"
}
```

## Usage Examples

### Using curl

**Create a new book:**
```bash
curl -X POST http://localhost:8080/api/v1/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Java Programming",
    "author": "Jane Smith",
    "price": 35.99,
    "categoryId": 1
  }'
```

**Update a book:**
```bash
curl -X PUT http://localhost:8080/api/v1/books/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Updated Java Programming",
    "author": "Jane Smith",
    "price": 39.99,
    "categoryId": 1
  }'
```

**Delete a book:**
```bash
curl -X DELETE http://localhost:8080/api/v1/books/1
```

**Get all books:**
```bash
curl http://localhost:8080/api/v1/books
```

**Search books:**
```bash
curl "http://localhost:8080/api/v1/books/search?keyword=Java"
```

### Using JavaScript (fetch)

**Create a book:**
```javascript
const newBook = {
  title: "JavaScript Guide",
  author: "Mike Johnson",
  price: 28.99,
  categoryId: 1
};

fetch('http://localhost:8080/api/v1/books', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify(newBook)
})
.then(response => response.json())
.then(data => console.log(data))
.catch(error => console.error('Error:', error));
```

**Update a book:**
```javascript
const updatedBook = {
  title: "Advanced JavaScript Guide",
  author: "Mike Johnson",
  price: 32.99,
  categoryId: 1
};

fetch('http://localhost:8080/api/v1/books/1', {
  method: 'PUT',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify(updatedBook)
})
.then(response => response.json())
.then(data => console.log(data))
.catch(error => console.error('Error:', error));
```

**Delete a book:**
```javascript
fetch('http://localhost:8080/api/v1/books/1', {
  method: 'DELETE'
})
.then(response => {
  if (response.status === 204) {
    console.log('Book deleted successfully');
  } else {
    console.error('Failed to delete book');
  }
})
.catch(error => console.error('Error:', error));
```

## Notes
- API hỗ trợ CORS cho tất cả origins (`@CrossOrigin(origins = "*")`)
- Tất cả endpoints đều trả về JSON
- Server mặc định chạy trên port 8080
- Database sử dụng MySQL (cấu hình trong application.properties)
