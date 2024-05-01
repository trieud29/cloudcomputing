# cloudcomputing

Model Training
  - Need to launch 4 EC2 Instances and Install spark,java,docker on all of them
  - One of them set to your master
  - Can be difficult with limited permissions in voclab
  - What I did was I had the datasets on my java project and used maven to compile everything
  - Now you need to start the spark-master and spark-workers
    - Those will be in the spark tar file downloaded
    - Run the training model on the master using spark submit and it will distribute work amongst the workers
    - AWS allows you to see the CPU usage amongst other things in the Dashboard
    - You can see the training module code in Homework2 in the main/src/java directory
    - I also imported the training and validation set to each EC2 instance open. Easily done with scp command
    - See environment file and set up for more info
   
    - Now application prediction,
        - First without Docker
          - A little more simple, the training module created a module that we can transfer to our separate instance and we packaged oour prediction code to a jar
          - Just like model training
          - We can simply run a spark-submit on this jar file and use our validationdataset as an argument just to make sure it works
        - Using docker can be a little trickier but easier in the long run.
        - Most of the work will be done in the DockerFile
          - Dockerfile copies the necssary files for one the trained model and the jar file when an image  is built
          - Also need to make sure docker file will take other csv files as an arugument when run so it can be easily tested later
          - After you are sure it runs i pushed it to docker hub.
    
