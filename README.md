vish_recsys
===========

--> Recommender system module for the Virtual Science Hub (ViSH) project.


--> The vish_recsys.jar can be executed with parameters as follows:

java -jar vish_recsys.jar {port}

Being {port} a number that specifies in which port will listen on the the HTTP server.
By default, the port number used is 8182.


--> REST services available and routes related to them:

1) Launch the Social Context generation process: 

http://localhost:{port}/recsys/socialcontext/generate


2) Discover the closest user's cluster considering a user id passed as a parameter as follows:

http://localhost:{port}/recsys/socialcontext/discoverusercluster?userid={number}
