# Cooperating and adversarial agents in the Hurricane Evacuation Problem

Using the Hurricane Evacuation problem simulator from the [first assignment](https://github.com/lina994/AI_Ass1 "link to simulator description") as a platform for implementing intelligent cooperating and adversarial agents.

Using the following principles:

* Artificial Intelligence:
    * Heuristic static evaluation function
    * α–β pruning algorithm
    * Adversarial (Minimax) and Cooperative game agents
* Dijkstra algorithm
* Minimum Heap
* OOP, Files I/O


## Game Environment

* Undirected weighted graph
* Types of agent actions:
    * Traverse
    * No-op (no-operation)
* The traverse action always succeeds if:
    * The edge (road) is unblocked
    * Time limit has not passed
* The game ends at the time limit T


## Agents types

* Human (i.e. read input from keyboard)
* Game tree search agent:
    * Adversarial (zero sum game)
        * Each agent aims to maximize its own score minus the opposing agent's score.
        * Using mini-max, with alpha-beta pruning.
    * A semi-cooperative game
        * Each agent tries to maximize its own score.
        * The agent disregards the other agent score, except that ties are broken cooperatively.
    * A fully cooperative
        * Both agents aim to maximize the sum of scores.


## Heuristic static evaluation function

Using heuristic can reduce the time and space needed to choose next action. Program use of the following heuristics:


### Adversarial Game

Heuristic of each node is:

    H = H1 - H2 , when:
    H1 = The maximum number of people that player can not save
    H2 = The maximum number of people that the opponent can not save

The next node that program expand will be with minimal H.


### Semi Cooperative Game

Heuristic of each node is:

    H1 = The maximum number of people that player can not save
    H2 = The maximum number of people that the opponent can not save

The next node that program expand will be with minimal H1. If there are several nodes with the same minimal H1, the program will select the one with the minimal H2.


### Fully Cooperative Game

Heuristic of each node is:

    H = The maximum number of people that both player and opponent can not save

The next node that program expand will be with minimal H.


## Running

### Input

file.txt file include graph description, people locations and deadline. For example:

    #T 6               ; number of vertices n in graph (from 1 to n)
    #E 1 2 W100        ; Edge from vertex 1 to vertex 2, weight 100
    #E 2 3 W1          ; Edge from vertex 2 to vertex 3, weight 1
    #E 2 5 W1          ; Edge from vertex 2 to vertex 5, weight 1
    #E 3 5 W5          ; Edge from vertex 3 to vertex 5, weight 5
    #E 5 6 W1          ; Edge from vertex 5 to vertex 6, weight 1
    #E 3 4 W1          ; Edge from vertex 3 to vertex 4, weight 1
    #V 4 P 1           ; Vertex 4 initially contains 1 person to be rescued
    #V 3 P 1           ; Vertex 3 initially contains 1 person to be rescued
    #V 5 P 3           ; Vertex 5 initially contains 3 persons to be rescued
    #V 2 S             ; Vertex 2 contains a hurricane shelter
    #D 15              ; Deadline is at time 15

<br>

### Graph visualization

![graph](https://github.com/lina994/AI_Ass2/blob/master/resources/input_example.png?raw=true "graph")
<br>

Additional input will be provided by the user via the terminal:

* number of agents
* type of game
* types of agents
* initial positions of agents
* parameter k
    * determining how much the vehicle is slowed due to load.
    * between 0 to 1


For example:

    Please enter the type of game
    For ADVERSARIAL game press 1
    For SEMI COOPERATIVE game press 2
    For FULLY COOPERATIVE game press 3
    > 1
    Please enter number of agents
    > 2
    Please enter the type of agent 1
    For HUMAN agent press 1
    For GAME TREE SEARCH agent press 2
    > 2
    Please enter the initial position of agent 1
    > 2
    Please enter the type of agent 2
    For HUMAN agent press 1
    For GAME TREE SEARCH agent press 2
    > 2
    Please enter the initial position of agent 2
    > 2
    Please enter the parameter k
    > 1


### Output

* Detailed output will be displayed in the terminal
* A summary will be saved in the results.txt  file


#### Score

The agent score is the number of people successfully evacuated by the agent.


## Authors

* Alina
    * [github](https://github.com/lina994 "github")
* Elina
    * [github](https://github.com/ElinaS21 "github")


## Official assignment description
[assignment 2](https://www.cs.bgu.ac.il/~shimony/AI2019/AIass2.html "assignment description")





