# easyM

<p align="center">
  <img src="https://user-images.githubusercontent.com/22354280/127648766-f1aaf70a-64c8-42f1-9c6e-a32699c80fa2.png">
</p>

The etymology of the name of this application refers to the nature of the application: "easy measures", in the sense that it will make it easy for the end user to create a large number of measure objects automatically almost instantly in Microsoft Power BI Desktop. 

For each measure, a series of "temporary" measures must be established (for example, a company's sales in the previous year: "sales" would consist of the "original" measure and the previous year's sales the temporary measure), and depending on the volume of information, the number of measures created one by one by the user can be astronomical. 

This process can become tedious and slow, so the use of easyM is intended to speed up the process. This application is configured as an external tool of Microsoft Power BI Desktop, establishing a connection with the model database, extracting information from it and inserting these temp measures in bulk from a "base" measurement.

The information extracted is needed to write the DAX expression that constitutes the measure. The model must be structured as a dimensional model: all information must be explicitly categorized in dimensions and facts.
