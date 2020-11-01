# Evaluation Functions/ Heuristics

pieceSquareTable[ ] =
 {

-80, -25, -20, -20, -20, -20, -25, -80,\
-25,  10,  10,  10,  10,  10,  10,  -25,\
-20,  10,  25,  25,  25,  25,  10,  -20,\
-20,  10,  25,  50,  50,  25,  10,  -20,\
-20,  10,  25,  50,  50,  25,  10,  -20,\
-20,  10,  25,  25,  25,  25,  10,  -20,\
-25,  10,  10,  10,  10,  10,  10,  -25,\
-80, -25, -20, -20, -20, -20, -25, -80

};


components of an evaluation function

which features or patterns are important?

* piece square table
* area
* mobility
* connectedness
* quad
* density

piece square table
a simple way to assign values to specific pieces on specific squares. Easy and fast to evaluate. Possible to experiment by using different tables which are created by changing the weight values.

area
determine the rectangular area which contains all the pieces. Less area is considered the better. The side with the least area is favored. 

mobility
The number of moves for each side are computed. It is possible that all moves are weighted equally. But, we can experiment by giving more weight to certain move types which are better than others (for example, a capture move). More mobility is considered the better.

connectedness
A piece is considered connected to another piece of the same color, if they are both adjacent to each other horizontally, vertically, or diagonally. For the evaluation function, we can consider the average number of connections. Alternatively, we can take into account the total number of connections. More connectedness is considered the better.

quad
A quad is a 2 X 2 rectangular area. For this assignment, we are going to consider only two types of quad: Q3 (a quad with three pieces) and Q4 (a quad with four pieces). For Q3, the remaining slot can be either empty or filled up with an enemy piece.

There are 49 possible quads (as we take into account only Q3 and Q4) to consider in a 8 X 8 grid. The evaluation function takes into account the total number of Q3 and Q4 quads for a side. The higher the quad count, the better for a side.

To speed up, we may calculate the center of mass of  a side and count only the quads that fall within a distance of at most two of the centre of mass. 


density
The density of the pieces can be calculated by a centre-of-mass approach. After determination of the center of mass of a side, we compute for each piece its distance to the centre of mass. For a measure of density, we may take the sum total of distances. Alternatively, the average distance can be taken into account. The less the distance, the higher the density which is considered favorable for a side. 

Experimentaion
Finally, you may experiment by taking some (or all) of these features and construct an evaluation function by taking a weighted sum or a weighted average.