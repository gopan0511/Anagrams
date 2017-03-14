/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;


public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;

    private static int wordLength = DEFAULT_WORD_LENGTH;

    private Random random = new Random();
    private ArrayList<String> wordList;
    private HashSet<String> wordSet;
    private HashMap<String,ArrayList<String>> lettersToWord;

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        //instantiate an arraylist
        wordList = new ArrayList<>();
        wordSet = new HashSet<>();
        lettersToWord = new HashMap<>();
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            //get the key for the word
            String key = keygen(word);
            ArrayList<String> anagrams;
            if(lettersToWord.containsKey(key)){
                anagrams = lettersToWord.get(key);
                anagrams.add(word);
            }
            else{
                anagrams = new ArrayList<>();
                anagrams.add(word);
                lettersToWord.put(key,anagrams);
            }
        }
    }

    public String keygen(String s){
        //returns the sorted form of words
        char[] letters = s.toCharArray();
        Arrays.sort(letters);
        return new String(letters);
    }

    public boolean isGoodWord(String word, String base) {
        //1.Check if it is a valid dictionary word
        //2.Check if word does not contain base
        if(wordSet.contains(word)){
            if(!word.contains(base)){
                return true;
            }
        }
        return false;
        //return wordset.contains(word) && !word.contains(base)
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        //compare each word in wordlist with target word and add to result
        for(String t: wordList) {
            if(keygen(t).equals(keygen(targetWord))){
                result.add(t);
            }
        }
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();

        //ListOfAnagram = []
        //FOR LOOP c in [A-Z]
        for(char i = 'a'; i <= 'z'; i++) {
           //1.Iterate and store it in temp from A-Z
            String tempWord = word + i;
            //2.Sort(word)----> SortedLetters
            String key = keygen(tempWord);
            //3.ListOfAnagrams = AnagramsHashMap.get(SortedLetters)
            ArrayList<String> anagrams;
            if(lettersToWord.containsKey(key)){
                anagrams = lettersToWord.get(key);
                result.addAll(anagrams);
            }
        }
        //Clean result and remove invalid anagrams
        ArrayList<String> solution = (ArrayList<String>) result.clone();
        for(String t:result){
            if(!isGoodWord(t,word)){
                solution.remove(t);
            }
        }
            //4.return ListOfAnagrams
        return solution;

    }

    public List<String> getAnagramsWithTwoMoreLetter(String word){
        ArrayList<String> temp;
        ArrayList<String> result = new ArrayList<String>();

        for(char i = 'a'; i<= 'z';i++){
            for(char j = 'a';j<='z';j++){
                String tempWord = word + i + j;
                String key = keygen(tempWord);

                ArrayList<String> anagrams;
                if(lettersToWord.containsKey(key)){
                    anagrams = lettersToWord.get(key);
                    result.addAll(anagrams);
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {

        int selectedIndex = random.nextInt(wordList.size()+1);
        String selectedWord = wordList.get(selectedIndex);

        if(getAnagramsWithTwoMoreLetter(selectedWord).size() >= MIN_NUM_ANAGRAMS ){
            return selectedWord;
        }
        else{
            for(int i= selectedIndex+1;i!=selectedIndex;i=(i+1)%(wordList.size())){
                selectedWord = wordList.get(i);
                return selectedWord;
            }
        }
        return "stop";
    }
}
