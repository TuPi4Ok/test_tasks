<a href="https://codeclimate.com/github/TuPi4Ok/test_tasks/test_coverage"><img src="https://api.codeclimate.com/v1/badges/a2cd67b3d2d4b317e506/test_coverage" /></a>
# postal_items
postal_items - is a REST API that allows tracking postal items.
The system registers postal items letters, parcels, their movement between post offices, and also implements the ability to obtain information and the entire history of movement of a particular postal item.

Also operations are realized: 

* registration of the mail item

* its arrival at an intermediate post office

* its departure from the post office

* its receipt by the addressee

* Viewing the status and complete history of the movement postal item

# Strat
```sh
./gradlew bootRun
```
# Start with docker
```sh
doker compose up
```
# Swagger documentation
By launching the application you can view the documentation at: http://localhost:5000/api/v1/swagger-ui/index.html
# Tech stack
* Spring framework (data, web)
* MongoDB
* JUnit 5
* Docker
* CI/CD with GitHub Actions
