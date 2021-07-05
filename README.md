# WordsCard
This app has three main functions:

1.  Dictionary - this function will display phonetics, audio, and definition of the searching word in the screen,
   which using retrofit to fetch data from third-part RESTful API, which will get the word's info in JSON format.
   Moreover, the Moshi library be used to parse JSON into kotlin object and vice versa.
      
     
2. Word List - it using the "recycler view" for displaying all the words that the user collected from the dictionary function,
   user can click the word in the list, it will turn to the dictionary function and display the word info.
   
3. Review - the function using "forgetting curve" helps the user to remember words.
   Firstly, the user will hear audio and see the word without definition of the word and the user can click "click for detail to see the definition of the word".
   Furthermore, clicking "know" if remember the word and the word will be shown on the screen in the next few days according to the forgetting curve.
   Or clicking "forget" the word will be randomly repeated on the screen until the user clicks "know" it.
   
   
https://user-images.githubusercontent.com/26683335/124438993-7494ff80-ddab-11eb-942c-cdb6d28d18e7.mp4
