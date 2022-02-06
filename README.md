
# Garaj Bileti Demo Uygulaması

# Gereksinimler
* Java JDK 8
* Maven
* IDE (Eclipse, InteliJ veya başka bir IDE)


# Proje Genel Yapisi

Demo Garaj Bileti uygulaması maven tipinde bir spring boot projesidir. 

# Bağımlılıklar

* Spring Boot Web 2.6.3
* Spring Boot Aop(Aspect Oriented Programming) 2.6.3
* Spring Boot DevTools 2.6.3(Geliştirme sürecinde kullanılan yardımcı paket)
* Test Bağımlılıkları ise Spring Boot Test 2.6.3 ve JUnit 4.13.2

## Projenin Paketleri

* Controller Paketi (com.garage.ticket.controller)

Projemizi dış dünyaya açan ve TicketController sınıfını  içeren pakettir. park, leave, status pathlerini içermektedir. 

> park = Araçların park talebini almaktadır. Parametre olarak plate(plaka), colour(renk), cartype(araç tipi) zorunlu parametreleri alır ve içerde gönderilen plaka var mı ve uygun  yer var mı kontrolü gerçeleştirilerek araca slot ataması  ve bilet verilmesi sağlanır.
> leave = Park etmiş aracın geçerli bilet numarasını parametre değeri  olarak alır ve ilgili memory de  cachelnen alanları  geri vermesi sağlamaktadır.
> status = Durum servisi çağrıldığında o anda memory cache mevcut listeyi json formatında dönmektedir.

* Data Transfer Object Paketi (com.garage.ticket.dto)

Bu pakette controller sınıfının parametre olarak  aldığımız  ve servisi kullanan kullanıcıya dönen nesneleri ve ayrıca domain modelindeki sınıfların tip dönüşümünü sağlayan metodu içeren sınıfları içermektedir.

> ParkCarRequest = park servisine gönderilen parametreleri içeren sınıftır.
> ParkCarStatusResponse = Garajda park etmiş araçların plaka, renk ve işgal ettiği slot değerlerini içeren parametre sınıfıdır.
> ParkCarDTOMapper = DB sınıfı ile DTO Sınıflarını birbirine çeviren metodları içeren sınıftır.
> ParkCarResponse = Bir araç garaja park edilmeye çalışıldığında dönen sonuç değerlerinin saklandığı sınıftır.

* Model Paketi (com.garage.ticket.model)
 
  Diğer paketlerdeki sınıfların kullandığı java bean bulunduğu pakettir.

> Car = InMemory veritabanında yani garajda  bulunan araçların tutulduğu sınıftır.

* Repository Paketi (com.garage.ticket.repository)

 Bu pakette veritabanında kullanılacak olan metodları içeren arayüzü içeren sınıftır. Bu sınıf gerçeklenerek DB ile ilgili işlemler gerçekleştirlecektir.

 > CarRepository = Araç Parkının gerçekleştirilmesi, araç parkı gerçekleştirilmeden önce kontrollerin gerçekleştirilmesi, garajda park etmiş araçların listeleme ve garajı terk eden araçların metod tanımlarını içeren arayüzdür.

 * Servis Paketi (com.garage.ticket.service)

 Kontrol  sınıfı ile repository sınıfını gerçekleyen DB sınıfı arasında köprü vaizfesi gören, iş mantıklarını içeren ve loğlama için gerekli methodların bulunduğu sınıfları içeren pakettir.

 > TicketService = Garaja park edecek aracın alanlarının kontrol edilmesi şayet bu doğrulamadan geçilmiş ise park işleminin gerçekleştirilmesi için repository gerçekleyen DB sınıfındaki  metodu çağıran, ayrıca o anki garajda park etmiş araçların listesini veren DB metodunu çağıran ve de garajdan çıkan araçların DB sınıfındaki araç terki metodunu çağıran metodları içeren sınıftır. 

 > LogService = Uygulamamıza  giren ve dönen değerlerin ve oluşan iç sunucu hatalarının hata izinin kaydının tutulabilmesi için gerekli  metodları içeren sınıftır.

 * Ayar Paketi (com.garage.ticket.util)

 Uygulamada, genel olarak kullanılan statik metodların bulunduğu sınıfı içeren pakettir.

 > GarageTicketUtil = Araç tipine göre aracın kapladığı slot genişliğini, paterni ve in-memory cache ortamında bulunan slotMap değerini güncelleyen metodların bulunduğu sınıftır.

* Sabitler Paketi (com.garage.ticket.constants)

> CarType = Araç tipinin bulunduğu  enum tipidir. Elemenları ise "Car - Jeep - Truck"
> LogType = Loğlama esnasında loğ çeşidinin tutulduğu enum tipidir. Elemanları ise "INFO - ERROR"
> GarageTicketConstants = Garajın içerdiği maksimum slot sayısı, bir sonraki araç ile arasında bulunan boşluk slot sayısı, Car Araç tipinin kapladığı slot değeri, Jeep Araç tipinin kapladığı slot değeri, Truck araç tipinin sakladığı slot değeri ve bunun yanı sıra bu araç tiplerinin slotMap içinde müsait alanların bulunabilmesi için gerekli olan pattern değerlerinin bulunduğu sabitlerin tutulduğu sınıftır.

* Database Paketi (com.garage.ticket.db)

Araç Listesini, Slot pattern değerini ve bilet listesini hafızada tutulmasını sağlayan sınıfı içeren pakettir.

> GarageTicketInMemoryDB = Araç Listesini, Slot pattern değerini ve bilet listesi düzenlenmesi, araç listesinin son halini veren ve araç listesinde aynı plaka değerinde bir araç var mı  kontrolünü içeren repository arayüzünü gerçekleyen sınıftır. Bu sınıfta, slot pattern string, garajdaki araç listesi ve boşta bulunan bilet listesini içeren statik değişkenleri de içermektedir.

* Aspect Oriented Programming Paketi (AOP -> com.garage.ticket.aop)

Bu paketteki sınıfların görevi, controller sınıfında ortak yapılması gereken işlere yoğunlaşılması sağlanması(loğlama) ve programda oluşabilecek herhangi bir iç sunucu hatasının yakalanarak yönetilmesi ve loğlanması sağlanır.

> GarageTicketAop = TicketController sınıfındaki tüm metodları çağırılmadan önce çağırılan methodu ve controller methodunu  çağırılması,    methoddan dönen sonuca  göre  gelen ve  dönen verilen loğlanması sağlanır. Böylelikle ileride  oluşabilecek mantıksal hata vb durumların  çözümünde kullanılabilmesi için bir fırsat sağlanmış olur.

> GarageTicketExceptionHandling = Garaj Bilet demo uygulmasında oluşabilecek herhangi bir hata durumunda burada  tanımlamış method akış yönlendirilek hatanın loğlanması ve kullanıcıya geri dönüş değerinin manüpüle edilerek dönüş yapılması sağlanmıştır.

## Uygulamanın Genel Çalışma Mantığı

### Aracın Garaja Park etmesi

- Öncelikle aracın talep nesnesinin plaka, renk ve araç tipi alanlarının girişini yapılıp yapılmaığı ve doğru şekilde araç tipinin servis kullanıcısı tarafından doğru şekilde gönderilip gönderilmediği ve gönderilen plaka değerinin aşağıdaki şekilde garajdaki araçlar arasında yer alıp almadığı kontrolü yapılır.
```java
if ((carList.stream().filter(c -> c != null && c.getPlate().trim().equalsIgnoreCase(plate.trim())).count()) > 0) {
	result = "the plate of car is in the garage!";
}
```   
- In-Memory veri tabanında aşağıdaki gibi 3 tane değişken tutulmaktadır. Birincisi garaj içindeki park edilmemiş boşta duran birimlerin paternini tutan **slotMap** değişkeni olup **String** tipindedir. Şayet **slotMap** boş ise o indeksdeki değeri 0(sıfırdır). İkincisi **carList** park edilmiş araçların bilet, plaka, renk ve kapladığı slot Listesini tutan **List** tipinde değişkendir. Başarılı şekilde park işlemini gerçekleştiren araç sahiplerine verilecek olan boştaki bilet listesini tutan **ticketNumberList** değişkeni **List** tipindedir. Değişkelere atanan varsayılan değerler garajın kapladığı maksimum slot 10(on) değerine göre verilmiştir. Şayet graja park edecek olan araçların tipi car ise garaj maksimum 5 tane araç içerilebilir ve  buna göre statik slotMap, ticketNumberlist ve carList başlangıç değişkenleri atanmıştır.
```java
static {
	slotMap = new String("0000000000");
	carList = new ArrayList<>(Arrays.asList(null, null, null, null, null));
	ticketNumberList = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5));
}
```
- Öncelikle garajda bulunan araçlar filtrelenir  ve kapladıkları alanları hesaplanır, sonrada  garaja park edecek aracın kaplayacağı slot sayısal değeri alınır  o aracın tipinin kapladığı slot pattern değeri slotMap içinde uygun pozisyon değeri bulunmuş ise sağ tarafında park edilmiş bir araç var ise bırakılacalak sabit boşluğu da  hesaba katıp (yok ise hesaba katılmaz) kalan boş slot sayısı hesaplanan kaplayacak alandan büyük veya eşit ise garaja park etme işlemine devam edilir yok ise garaj dolu uyarısı verilebilmesi için işleme son verilir.
```java
/*Garajda bulunan araçları filtrele*/
List<Car> carFilteredList = carList.stream().filter(c -> c != null).collect(Collectors.toList());
```
```java
/*Garajda kullanılan slotları hesapla*/
int totalUsingSlot = 0;
for (Car car : carFilteredList) {
	totalUsingSlot += GarageTicketUtil.getCarTypeSlotLength(car.getCarType())+ GarageTicketConstants.NEXT_ONE_EMPTY_SLOT_VALUE;
}
```
```java
/*Park edilecek aracın kaplayacağı slot genişliği ve pattern değerini al sonrada slotMap içinde bu paternin indeks değeri var mı yoksa işlemi sonlandır.*/
int slotLengthOfTheCar = GarageTicketUtil.getCarTypeSlotLength(carType);
String willSearchPattern = GarageTicketUtil.getCarTypeSlotPattern(carType);
		
int indexOfPattern = slotMap.indexOf(willSearchPattern);
if(indexOfPattern < 0) {
	return 0;
}
```
```java
/*SlotMap içinde araç tipinin slot patern değerinin bulunduğu başlangıç indeks değeri ile araç tipinin slot genişliği değerini topla ve şayet slotMap içinde bu toplam değerindeki indeks değeri 1(Bir) eşit ise yani park edilecek aracın sağında park etmiş araç var ise var sayılan boş bırakma slot değerini de hesaba kat.*/
int controlIndex = (indexOfPattern + slotLengthOfTheCar);
int addedNextSlotValue = 0;
if (controlIndex < slotMap.length() && "1".equals(slotMap.substring(controlIndex, controlIndex + 1))) {
	addedNextSlotValue = GarageTicketConstants.NEXT_ONE_EMPTY_SLOT_VALUE;
}
```
```java
/*Boş kalan slot değeri şayet araç tipinin kapladığı alan ile sağında park etmiş araç var ise bırakılacak boş alan toplamından büyük veya eşit ise park etme işlemine başla eşit değlse bu araç park edilemez ve garaj dolu uyarısı için işlemi sonlandır*/
if ((GarageTicketConstants.MAX_GARAGE_SLOT_COUNT - totalUsingSlot) >= 
		(slotLengthOfTheCar	+ addedNextSlotValue)) {/* Available Slot Exists */
 ...
}
```
- ticket listesinde  kalan en küçük bilet değerini al. DTO Mapper ile request değerini Car Model çevir ve verilecek bilet değerini model sınıfına ata ayrıca aracın tutacağı slot değerlerini modelin slot listesine ata. Statik slotMap değerini kullanılan alana göre 0 -> 1 (Kullanılmış slot alanına)dönüştür(Not: Sol taraf gark girişini temsil eder). carListesine car Model değerini set et. Sonrada verilen bilet değerini boştaki bilet listesinden çıkar.
```java
/*Boştaki bilet listesindeki En küçük bilet değerini al*/
int ticketNum = ticketNumberList.stream().mapToInt(v -> v).min().getAsInt();
```
```java
/*Park Araç talep nesinesi Car Model nesine dönüştür verilen bilet değerini ata sonra slot listesini ata.*/
Car parkingCar = ParkCarDTOMapper.convertParkCarReq(carReq);
parkingCar.setTickeNum(ticketNum);
int slotValue = indexOfPattern + 1;
for (int j = 0; j < slotLengthOfTheCar; j++) {
	parkingCar.getSlotList().add(slotValue++);
}
```
```java
/*slotMap içinde olası kullanılan alanları 0 -> 1 çevir*/
slotMap = GarageTicketUtil.changeSlotMap(slotMap, '1', indexOfPattern,(slotLengthOfTheCar + indexOfPattern + GarageTicketConstants.NEXT_ONE_EMPTY_SLOT_VALUE));		
```
```java
/*park eden arabayı, park etmiş arabalar listesine ekle: Önce Listedeki ilk boş indeks değerini bul  ve listede blunan sıraya model değerini atamayı yap. */
int carIndex = carList.indexOf(null);
carList.set(carIndex, parkingCar);	
```
```java
/*Kullanılan bilet değerini boştaki bilet listesinden çıkar.*/
ticketNumberList.remove(new Integer(ticketNum));
```

### Aracın Garajı Terk Etmesi

- Öncelikle garajı terk edecek arabanın sahip olduğu bilet numarası değeri, servisi kullanan kullanıcı tarafından servise path değişkeni olarak gönderilmelidir.

- Gönderilen bilet değerine sahip araç grajda var mı kontrolü gerçekleştirilir.
```java
/*Gönderilen bilet değerine ait garaja araç var mı kontrolü. Yoksa işlem sonlandırılır.*/
Optional<Car> optWillLeaveCar = carList.stream().filter(c -> c != null && c.getTickeNum() == ticketNum).findFirst();
if (optWillLeaveCar.isPresent()) {
    ...
}
```
- Aracın park araç listesindeki indeks değeri alınır. Başlangıç slot indeks değeri alanır aracın kapladığı slot genişliği bulunarak, daha sonra slotMap değeri 1 -> 0 olarak güncellenir. Sonra park edilmiş araba Listesi boşaltılır. Boştaki Ticket Listesine  ticket değerini tekrardan ekler ve sonrada boştaki bilet listesi sıralanır.
```java
/*ArabaModeli alınır. park edilmiş araç Listesindeki indeks değeri bulunur. Aracın Slot listesindeki ilk değeri alıp indeks değeri hesaplanır. Aracın kapladığı slot genişliği değeri alınır.*/
Car willLeaveCar = optWillLeaveCar.get();
int indexOfCar = carList.indexOf(willLeaveCar);
int slotIndex = willLeaveCar.getSlotList().get(0) - 1;
int slotLengthOfTheCar = GarageTicketUtil.getCarTypeSlotLength(willLeaveCar.getCarType());
/*slotMap String değişkeni bu sefer 1 -> 0 güncellenir.*/
slotMap = GarageTicketUtil.changeSlotMap(slotMap, '0', slotIndex,(slotLengthOfTheCar + slotIndex + GarageTicketConstants.NEXT_ONE_EMPTY_SLOT_VALUE));		
/*Park edilmiş araç Listesindeki ilgili indeks (sıradaki) değeri null olarak güncelle*/
carList.set(indexOfCar, null);
/*Boştaki ticket listesi aracın tiket değerinş ekle ve tekrardan listesyi küçükten büyüğe sırala.*/
ticketNumberList.add(ticketNum);
ticketNumberList = ticketNumberList.stream().sorted().collect(Collectors.toList());
```

## Thread Senkronizayonu  ve Veri Bütünlüğünün Sağlanması(Consistency)

- Thread Senkronizasyonu ReentrantLock locksınıf ile  hem park hem de leave servis methodlarında kullanılarak concurrent olarak çağırılan işlemlerde veri bütünlüğünün sağlanması için kullanılmıştır. Garaja park etme ve garajı terk etme işlemi sırayla çalışarak veri bütünlüğü sağlanmış olur.

```java
public ParkCarResponse parkCar(ParkCarRequest carReq) {

		lock.lock();
		try {

			ParkCarResponse result = new ParkCarResponse();
			result.setValidationMessage(this.validateCarInput(carReq));
			if (result.getValidationMessage().isEmpty()) {
				result.setNumberOfSlots(this.ICarRepository.park(carReq));
			}

			return result;
		} finally {
			lock.unlock();
		}

}

public boolean leaveCar(int ticketNum) {

		lock.lock();
		try {
			return this.ICarRepository.leave(ticketNum);
		} finally {
			lock.unlock();
		}

}
```

## SOLID prensipleri ve Kullanılan Tasarım Kalıbı ve Exception handling

- Repository interface tanımlanmış ve bunu gerçekleyen **GarageTicketInMemoryDB** sınıfı oluşturulmuş olup, bu repository implemente eden **GarageTicketInMemoryDB** sınıfının bir tane örneği  Ticket Servis sınıfının constructor oluşturulmuştur. Bilindiği gibi spring boot da sınıfların scope değeri vasayılan singletondır. Böylece Factory design pattern kulanılmıştır ve solid prensiplerinde dependency inversion prensibine sadık kalınmıştır. Bu da ileride  buradaki repository implemente eden DB sınıflarını artırarak hem ilişkisel veri tabanı ile hem de ilişkisel olmayan veritabanı veya dağıtık memory uygulamalarına kolaylıkla bağlanacak şekilde geliştirilebilecek imkan sağlanmıştır. 

- Single responsibility için Loğlama AOP(Aspect Oriented Programming) kullanılmıştır. Burada **GarageTicketAop** sınıfı kullanmıştır tüm controller metodları için ortak yapılacak işlemler burada gerçekleştirilebilir.

- Exception Handling **GarageTicketExceptionHandling** sınıfı kullanılmış olup generalExceptionHandling metounda  @ExceptionHandler annotasyonuna Exception.class parametre değeri verildiğinden garaj ticket demo uygulmasında oluşaiblecek tüm hatalar  bu metoda  yönlendirilir ve böylece tek bir merkezden loğlama ve kullanıcıya verilecek hata mesajı ayarlanabilecek duruma getirilmiştir.

### Logback Loğlama Ayarları

- logback.xml dosyasında loglama ile ilgli ayarlar  mevcuttur. Dosya /src/main/resources klasörünü altındadır.
- com.garage.ticket paketi altındaki tüm sınıflardan oluşan loğlar  garageticketservice.log dosyasına kayıtlanır log dosyası uygulamanın çalıştığı bir üst dizindeki ../logs/ klasörü altında  oluşur. Ayrıca log dosyaları tarihsel olarak ../logs/archived/ dizini altında 1MB boyutunu geçmeyecek ve 10 günlük arşiv süresi sınırı ile kural tanımı oluşturulmuştur.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
   <property name="HOME_LOG" value="../logs/garageticketservice.log"/>
   <appender name="FILE-ROLLING" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${HOME_LOG}</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>../logs/archived/garageticketapp.%d{yyyy-MM-dd}.log</fileNamePattern>
            <totalSizeCap>1MB</totalSizeCap>
            <maxHistory>10</maxHistory>
        </rollingPolicy>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <charset>UTF-8</charset>
            <pattern>%d %p %c{1.} [%t] %m%n</pattern>
        </encoder>
    </appender>
    <logger name="com.garage.ticket" level="info" additivity="false">
        <appender-ref ref="FILE-ROLLING"/>
    </logger>
</configuration>
 ```

## Uygulamanın Ayağa Kaldırılması

- Projenin bulunduğu dizindeki yani pom.xml dosyasının bulunduğu dizinde console uygulaması açılır. Aşağıdaki komutlar çalıştırılır.
```sh
mvn clean install
mvn spring-boot:run
```
- Uygulama ayağa kalktığında **8080** portunu dinlemeye başlar.(http://localhost:8080/)  
- Uygulamayı projenin kök dizinindeki **GarageTicketServiceCollection.postman_collection.json** dosyasını, postman uygulamasından import edilerek test edilebilir. Ayrıca uygulama maven install aşamasında iki tane birim testinden de geçmektedir.