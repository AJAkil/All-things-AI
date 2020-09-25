from graph import *


# a = Vertex('A')
# b = Vertex('B')
# c = Vertex('C')
# d = Vertex('D')
# e = Vertex('E')

# a.add_neighbors_undirected([b,c,e]) 
# b.add_neighbors_undirected([a,c])
# c.add_neighbors_undirected([b,d,a,e])
# d.add_neighbor_undirected(c)
        
        
# g = Graph()
# g.add_vertices([a,b,c,d,e])
# g.add_edge_undirected(b,d)
# ass = Vertex('ass')
# pasa = Vertex('Pasa')
# g.add_vertices([ass, pasa])
# g.add_edge_undirected(ass, pasa)
# g.add_edge_undirected(ass,d)
# g.add_edge_undirected(c, pasa)
# g.print_graph()
g = Graph()
a = Vertex(0)
b = Vertex(1)
c = Vertex(2)
d = Vertex(3)
e = Vertex(4)
g.add_vertices([a,b,c,d,e])
g.add_edge_undirected(a,b)
g.add_edge_undirected(a,c)
g.add_edge_undirected(b,c)
g.add_edge_undirected(b,e)
g.add_edge_undirected(c,e)
g.add_edge_undirected(e,d)
g.print_graph()
g.greedy_color()
g.print_allnode_colors()

#f = open('yor-f-83.stu','r+')
#g.construct_graph(f)
#g.print_graph()

# print(len([n.name for n in g.vertices['0031'].neighbors]))
# print(set([n.name for n in g.vertices['0031'].neighbors]))
            