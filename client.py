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
# a = Vertex(1)
# b = Vertex(2)
# c = Vertex(3)
# d = Vertex(4)
# e = Vertex(5)
# f = Vertex(6)
# g.add_vertices([a,b,c,d,e,f])
# g.add_edge_undirected(a,b)
# g.add_edge_undirected(a,b)
# g.add_edge_undirected(a,b)
# g.add_edge_undirected(c,d)
# g.add_edge_undirected(c,d)
# g.add_edge_undirected(c,d)
# g.print_graph()
# g.add_edge_undirected(b,e)
# g.add_edge_undirected(c,e)
# g.add_edge_undirected(e,d)
# g.print_graph()
# g.greedy_color()
# g.print_allnode_colors()

#f = open('car-s-91.stu','r+')
#f = open('ear-f-83.stu','r+')
f = open('yor-f-83.stu','r+')
#f = open('test.txt','r+')
g.construct_graph(f)
# print(max(list(g.get_all_vertices_degree().values())))
#g.print_graph()
g.greedy_color(choice='degree_sorted')
#g.print_all_neighbor_color()
f = open('yor-f-83.stu','r+')
g.cal_avg_penalty(f)
print(g.avg_penalty)
#g.print_allnode_colors()



            