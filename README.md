# Cooperating and adversarial agents in the Hurricane Evacuation Problem

Using the Hurricane Evacuation problem simulator from the [first assignment](https://github.com/lina994/AI_Ass1 "link to simulator description") as a platform for implementing intelligent cooperating and adversarial agents.

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


