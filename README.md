# Top-Up System - E-Belge Gönderim Sistemi

Kullanıcıların e-Fatura ve e-Arşiv gönderebildiği, kontör tabanlı bir E-Belge Gönderim Sistemi.

## 🚀 Özellikler

- **Kullanıcı Yönetimi**: JWT tabanlı authentication ve role-based access control
- **Firma Yönetimi**: Her kullanıcı kendi firmasını tanımlayabilir
- **Kontör Sistemi**: Wallet tabanlı kontör yükleme ve harcama
- **Belge Gönderimi**: e-Fatura ve e-Arşiv belgelerini entegratör API'si üzerinden gönderme
- **Admin Panel**: Kullanıcı, firma, işlem ve belge yönetimi
- **Webhook Desteği**: Entegratörden gelen webhook'ları işleme

## 📋 Teknolojiler

### Backend
- Java 17+
- Spring Boot 3.x
- Spring Security (JWT)
- Spring Data JPA
- PostgreSQL
- MapStruct
- FeignClient
- OpenAPI/Swagger

### Frontend
- Next.js 15 (App Router)
- TypeScript
- TailwindCSS
- React Server Components

## 🛠️ Kurulum

### Backend

1. PostgreSQL veritabanını oluşturun:
```sql
CREATE DATABASE topup_db;
```

2. `application.yml` dosyasındaki veritabanı ayarlarını düzenleyin:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/topup_db
    username: postgres
    password: postgres
```

3. Backend'i çalıştırın:
```bash
cd top-up_backend/top-up
./mvnw spring-boot:run
```

Backend `http://localhost:8080` adresinde çalışacaktır.

Swagger UI: `http://localhost:8080/swagger-ui.html`

### Frontend

1. Bağımlılıkları yükleyin:
```bash
cd top-up-frontend
npm install
```

2. `.env.local` dosyası oluşturun:
```env
NEXT_PUBLIC_API_URL=http://localhost:8080/api
```

3. Frontend'i çalıştırın:
```bash
npm run dev
```

Frontend `http://localhost:3000` adresinde çalışacaktır.

## 📚 API Endpoints

### Auth
- `POST /api/auth/register` - Kullanıcı kaydı
- `POST /api/auth/login` - Giriş

### Company
- `POST /api/companies` - Firma oluştur
- `GET /api/companies/me` - Firma bilgilerini getir

### Wallet
- `GET /api/wallet/me` - Wallet bilgilerini getir
- `POST /api/wallet/load` - Kontör yükle (Admin)

### Credit Packages
- `GET /api/packages` - Aktif paketleri listele
- `POST /api/packages` - Paket oluştur (Admin)

### Documents
- `POST /api/documents` - Belge oluştur
- `POST /api/documents/{id}/send` - Belge gönder
- `GET /api/documents` - Belgeleri listele
- `GET /api/documents/{id}` - Belge detayı

### Webhook
- `POST /api/webhook/entegrator` - Webhook al

### Admin
- `GET /api/admin/users` - Tüm kullanıcılar
- `GET /api/admin/companies` - Tüm firmalar
- `GET /api/admin/transactions` - Tüm işlemler
- `GET /api/admin/documents` - Tüm belgeler

## 🔐 Güvenlik

- JWT token tabanlı authentication
- Role-based access control (USER, ADMIN)
- Password encryption (BCrypt)
- CORS yapılandırması
- Optimistic locking (kontör düşümü için)

## 📝 Kullanım Senaryosu

1. **Kullanıcı Kaydı**: `/register` ile yeni kullanıcı oluştur
2. **Firma Tanımlama**: `/companies` ile firma bilgilerini gir
3. **Kontör Yükleme**: Admin panelinden kontör paketi satın al
4. **Belge Oluşturma**: `/documents` ile e-Fatura/e-Arşiv oluştur
5. **Belge Gönderme**: `/documents/{id}/send` ile belgeyi gönder
6. **Kontör Düşümü**: Başarılı gönderimde otomatik kontör düşümü

## 🧪 Test

Postman collection dosyası: `top-up_backend/top-up/Top-Up-System.postman_collection.json`

Collection'ı Postman'e import edip test edebilirsiniz.

## 📄 Lisans

Bu proje eğitim amaçlıdır.

