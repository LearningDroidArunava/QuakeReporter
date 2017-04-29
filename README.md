# QuakeReporter

An Android app that allows user to view quakes that took place during any period of time.

# Usage

Just set up the Date and time and other fields in the search page and the app would return the all of the earthquakes that took place 
during that period of time. 

# Note

1) The app uses the [USGS API](https://earthquake.usgs.gov/fdsnws/event/1/) API to display all te quakes that took place.
2) While quering if the number of earthquakes during the mentioned time is greater than 20000 then USGS would return an error
   and that is why the limits the number of earthquakes to 20000 on each request, unless specfied by the user explicitly.
   
# License

The project is distributed under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0)
