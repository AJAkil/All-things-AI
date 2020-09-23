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
f = open('yor-f-83.stu','r+')
g.construct_graph(f)
g.print_graph()

print([n.name for n in g.vertices['0031'].neighbors])
print(set([n.name for n in g.vertices['0031'].neighbors]))
            