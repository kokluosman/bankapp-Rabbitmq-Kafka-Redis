# BankApp

Bu projede Redis,Kafka ve RabbitMQ kullanarak bir kuyruk mekanizması kullandım. Ayrıca H2 Databese ve Thymeleaf kullandım.
Proje içerik olarak küçük bir banka uygulamasını canlandırmaktadır.
Projede kullandığım katmanları ve görevlerini aşağıda sıralayarak açıkladım.

### 1.Model/Entities
Bu katmanda Customer,Address,Account ve Cities adlı 4 tane entity oluşturdum. Bunlar haricinde 2 tane Enum tanımladım ve veritabanı tablolarımı oluşturdum.

### 2.Repository/DAO Katmanı
Bu katmanda Model sınıfında oluşturduğum entitiy'ler için Repository Katmanı oluşturum ve JPA Repository kullandım.

### 3.Configuration Katmanı
Bu katmanda Redis ve Kafka için Configuration ayarlarını yaptım.

### 4.Service/Business Katmanı
Bu katmanda iş kodlarımı yazdım. Datanın hangi şartlarda nasıl transfer edileceğini belirledim.

### 5.Exception Katmanı
Bu katmanda CustomerNotFoundException ve GeneralException adında 2 tane hata yakalama sınıfı yazdım.

### 6.DTO Katmanı
Bu katmanda DTO'larımı ve ayrıca gelen request'leri yazdım. Bunlar içerisinde converter'lar yazarak service katmanında ki yükümü azalttım.

### 7.API/Controller
Bu katmanda Account,Cities,Customer için End-Pointleri yazdım.

### 8.AccountService Katmanı için Junit5 kullanılarak unit test yazdım.
