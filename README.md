# Sezamo REST app

## TEST
`mvn test`

## BUILD AND RUN DOCKER COMPOSE
```
mvn clean package
docker-compose up --build -d
```

## Sezamo APP COMES WITH SOME DEFAULT DATA
| id | name | price | stock |
| :--- | :--- | :--- | :--- |
| 10000 | Mutti Pelati 2 x 400 g | 1.46 | 20 |
| 10001 | Nivea Sun protect & hydrate 30 Alta 200 ml | 11.96 | 10 |
| 10002 | Sammontana Barattolino Classici Croccantino 500 g | 2.87 | 13 |
| 10003 | Pizzetta Margherita | 0.88 | 10 |
| 10004 | Pizzetta Napoletana | 0.94 | 10 |
| 10005 | Pizzetta Quattro Formaggi | 1.05 | 10 |
| 10006 | Pizzetta Calzone | 0.83 | 10 |
| 10007 | Pomodoro Cuore di Bue | 1.5 | 2 |
| 10008 | Taralli finocchio e sesamo | 3.35 | 10 |
| 10009 | aMARE CON GUSTO Orata Eviscerata fresca | 5.99 | 1 |


## STOP DOCKER COMPOSE
`docker-compose down`

## REST ENDPOINTS
There is a .json Postman collection named `Rohlik Group.postman_collection.json` in the root of the project you can use to test the endpoints.

## SWAGGER
You can check the swagger documentation [here](http://localhost:8080/swagger-ui/index.html#/) when app is running.