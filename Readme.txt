Sean Ennis
sennis3, 653900061
CS 411
Assignment 7 - IDA* Search
March 30, 2020

Navigate to the bin directory in sennis3_idastar and run it with "java IDAStarSearch"

The program will prompt for the initial configuration state of the tiles as a list of 16 numbers (0-15) with a space between each number. The 0 represents the empty space on the board.

The goal state would be represented as: 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 0

The program will then ask if the user would like to skip the misplaced tiles heuristic. This is intended for the more complex test cases. The user enters 'y' for "yes" or 'n' for "no".

If the user enters 'n', the program will run the search twice with two different heuristics: misplaced tiles and Manhattan distance. The results for the misplaced tile heuristic is displayed first with the Manhattan distance results after that.

If the user enters 'y', the program will only run the search with the Manhattan distance heuristic.

The program will only run one test case at a time, so if you want to try a another initial state, the program needs to be run again.

I implemented my search tree so that it would expand child nodes in the order up, down, left, and right.

