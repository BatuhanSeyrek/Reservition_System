# Reservition_System 🪑📅

**Reservition_System**, Java Spring Boot ile geliştirilmiş, JWT tabanlı güvenlik mekanizmasına sahip bir randevu/rezervasyon sistemidir. Kullanıcı ve yönetici (admin) rollerine göre yetkilendirme yapılmakta, koltuk rezervasyonları üzerinden işlem sağlanmaktadır. Proje, çoklu şirket desteği (multi-tenant yaklaşıma hazırlık), kullanıcı bildirimleri, temiz kod mimarisi ve test altyapısıyla kurumsal düzeyde geliştirilmeye uygundur.

---

## 🎯 Projenin Amacı

Geliştirilen bu sistem ile belirli bir hizmet veya alan üzerinde kullanıcıların koltuk/randevu rezervasyonu yapması, bu işlemlerin admin paneli tarafından yönetilmesi ve kullanıcıların zamanında bilgilendirilmesi hedeflenmiştir. Uygulama, özellikle ofis içi kaynak planlaması, salon/oda rezervasyonu gibi sistemler için temel bir altyapı sunar.

---

## 🚀 Temel Özellikler

### 🔐 JWT Authentication
- Kullanıcı ve admin girişlerinde token tabanlı kimlik doğrulama.
- Spring Security ile güvenli endpoint erişimi.

### 👥 Rol Tabanlı Kullanıcı Ayrımı
- `USER` ve `ADMIN` rolleri ayrı işlemler gerçekleştirebilir.
- Admin kullanıcıları koltukları ekleyebilir/silebilir.
- Kullanıcılar uygun koltuklara rezervasyon oluşturabilir.

### 📅 Randevu Yönetimi
- Koltuklara günün belirli saatlerine uygun randevu alınabilir.
- Sistemde işlem süresi baz alınarak uygunluk kontrolü yapılır.

### 📧 Bildirim Servisi
- Kullanıcılara randevu saatinden 30 dakika önce otomatik e-posta bildirimi gönderilir.
- Spring `@Scheduled` anotasyonu ile zamanlayıcı sistem entegre edilmiştir.

### 🏢 Multi-Tenant Yapıya Uyumlu Tasarım
- Her kullanıcı veya koltuk bir `company_id` ile ilişkilidir.
- Bu sayede ileride çoklu şirket destekli bir SaaS mimarisi kurulabilir.

### 🧪 Unit Test Desteği
- JUnit kullanılarak temel servis katmanları test edilmektedir.
- Testler sayesinde daha güvenilir bir geliştirme süreci sağlanır.

---

## 🧰 Kullanılan Teknolojiler

| Teknoloji      | Açıklama |
|----------------|----------|
| Java 17        | Projenin yazıldığı ana dil |
| Spring Boot    | Hızlı ve yapılandırılmış web uygulaması çatısı |
| Spring Security| Kimlik doğrulama ve yetkilendirme |
| JWT            | Token bazlı oturum yönetimi |
| JPA (Hibernate)| ORM (Nesne-ilişkisel eşleme) |
| PostgreSQL     | Veritabanı yönetim sistemi |
| Lombok         | Kod tekrarı azaltma (Getter/Setter vs.) |
| Maven          | Proje bağımlılık yönetimi |
| JavaMailSender | E-posta servisi |
| JUnit          | Birim testi çatısı |
