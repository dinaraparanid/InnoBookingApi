**InnoBookingApi**
-----------------

[![Kotlin](https://img.shields.io/badge/kotlin-1.9.0-blue.svg?logo=kotlin)](http://kotlinlang.org)

## **Developer**
[Paranid5](https://github.com/dinaraparanid)

## **About App**
API for [InnoBookingBot](https://github.com/dinaraparanid/InnoBookingBot).

### **Requests**

Requests are available as [.yaml file](inno-booking-api-v0.1.0.yaml)
that can be parsed with [Swagger](https://editor.swagger.io/)

#### **Short description**

<ul>
    <li><b>GET</b> /rooms - Get All Bookable Rooms</li>
    <li><b>POST</b> /rooms/free - Get Free Rooms</li>
    <li><b>POST</b> /rooms/{room_id}/book</li>
    <li><b>POST</b> /bookings/query - Bookings filter (by dates, rooms, and users)</li>
    <li><b>DELETE</b> /bookings/{booking_id} - delete a booking</li>
</ul>

## **Setup**

API can be deployed with docker:

```shell
docker build -t innobookingapi .
docker run -p <port>:<port> innobookingapi
```

Next environmental variables should be provided:

```
BOT_TOKEN='your bot token'
EMAIL_AUTH='email sender, e.g. i.ivanov@innopolis.university'
EMAIL_AUTH_PASSWORD='email password'
DATABASE_URL='firestore database url'
PROJECT_ID='firebase project ID'
CREDENTIALS_PATH='firebase project .json file'
```

If you want to build api on your machine explicitly, you can use gradle:

```shell
./gradlew build
./gradlew run
```

## **Stack**

<ul>
    <li>Kotlin 1.9</li>
    <li>Coroutines + StateFlow</li>
    <li>Ktor (Core, Netty, Cors, Compression, Content Negotiation + JSON)</li>
    <li>Exposed ORM (DAO + Java Time extensions)</li>
    <li>JDBC</li>
    <li>KotlinX.Serialization</li>
    <li>KotlinX.DateTime</li>
    <li>Java Firebase Admin (Firestore integration)</li>
    <li>Kotlin Dotenv</li>
    <li>Logback</li>
</ul>

## **License**
*GNU Public License V 3.0*
