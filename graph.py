from vertex import Vertex
import random as rd
from queue import Queue

class Graph:
    def __init__(self):
        self.vertices = {} # A dictionary containing node name and object as value
        self.no_of_vertices = 0
        self.avg_penalty = -1
        self.colors_needed = 0


    def add_vertex(self, vertex):
        if isinstance(vertex, Vertex) and vertex.name not in self.vertices:
            self.vertices[vertex.name] = vertex
            self.no_of_vertices += 1
            return True
        else:
            return False

    def add_vertices(self, vertices):
        for vertex in vertices:
            self.add_vertex(vertex)

    def add_edge_undirected(self, vertex_from, vertex_to):
        if isinstance(vertex_from, Vertex) and isinstance(vertex_to, Vertex):
            if vertex_from.name in self.vertices and vertex_to.name in self.vertices:
                self.vertices[vertex_from.name].add_neighbor_undirected(vertex_to)
                return True
            else:
                return False
        else:
            return False

    def add_edge_directed(self, vertex_from, vertex_to):
        if isinstance(vertex_from, Vertex) and isinstance(vertex_to, Vertex):
            if vertex_from.name in self.vertices and vertex_to.name in self.vertices:
                self.vertices[vertex_from.name].add_neighbor_directed(vertex_to)
                return True
            else:
                return False
        else:
            return False

    def print_graph(self):
        for key in self.vertices.keys():
            print(str(self.vertices[key]))
            print()

    def get_all_vertices_degree(self):
        return {key: value.get_vertex_degree() for key, value in self.vertices.items()}

    def construct_graph(self, file_object):
        for row in file_object:
            courses = row.split()
            
            # we form the vertex object if they are not already made
            for index, c in enumerate(courses):
                if c not in self.vertices:
                    # if it is not in our graph, we create a vertex object
                    course_vertex = Vertex(c)
                    self.add_vertex(course_vertex)

            for i, course in enumerate(courses):
                for j, neighbor in enumerate(courses[i+1:]):
                    self.vertices[course].add_neighbor_undirected(self.vertices[neighbor])
                    #print(self.vertices[course])

    def sort_by_degree(self):
        vertices_list = [value for value in self.vertices.values()]
        vertices_list.sort(key=lambda x: x.get_vertex_degree(), reverse=True)

        for vertex in vertices_list:
            print(vertex.name, vertex.get_vertex_degree())

    def greedy_color(self, choice='default'):

        max_colors = set()
        vertices_list = []

        if choice == 'random':
            vertices_list = list(self.vertices.keys())
            rd.shuffle(vertices_list)
        elif choice == 'degree_sorted':
            vertices_list = [value for value in self.vertices.values()]
            vertices_list.sort(key=lambda x: x.get_vertex_degree(), reverse=True)
            vertices_list = [vertex.name for vertex in vertices_list]
        elif choice == 'default':
            # we get the list of all the vertices of the graph serially
            vertices_list = list(self.vertices.keys())


        # a temporary list to store the available colors
        # true for colors[index] means that the indexed vertex color is true
        # used for checking the colors of the neghbors of a certain vertex
        colors = [False for _ in vertices_list]

        # we assign first color to the first vertex
        self.vertices[vertices_list[0]].color = 0

        # now we assign colors to the reamining v-1 vertices
        for i in range(1, self.no_of_vertices):
            # we process all adjacent vertices of ith vertex and flag their colors as not available
            for neighbor in self.vertices[vertices_list[i]].neighbors:
                if self.vertices[neighbor.name].color != -1:
                    colors[self.vertices[neighbor.name].color] = True # marking the neighbor's color as True means unavailable.

            # find the first available color
            available_color = -1
            for index, state in enumerate(colors):
                if state == False:
                    available_color = index
                    break


            # assign the color
            self.vertices[vertices_list[i]].color = available_color

            # keep track of the max color
            max_colors.add(available_color)

            # resetting the value to false for next iteration
            for neighbor in self.vertices[vertices_list[i]].neighbors:
                if self.vertices[neighbor.name].color != -1:
                    colors[self.vertices[neighbor.name].color] = False

        #print('Total time slots: ', max(max_colors)+1)
        self.colors_needed = max(max_colors) + 1

    def reset_marker(self):
        for node in self.vertices.values():
            node.checked = False

    @staticmethod
    def alter_color(colors, color_to_alter):
        if colors[0] == color_to_alter:
            return colors[1]
        elif colors[1] == color_to_alter:
            return colors[0]


    def operate_kempe_chain(self):
        
        # Declare the Queue to operate
        q = Queue()

        # Choose two random colors and save it to a list
        vertices_list = [node for node in self.vertices.values()]
        random_node = rd.choice(vertices_list)

        # Handling an edge case here
        if len(random_node.neighbors) == 0:
            while True:
                random_node = rd.choice(vertices_list)
                if len(random_node.neighbors) > 0:
                    break

        random_node2 = random_node.neighbors[0]
        allowed_colors = [random_node.color, random_node2.color]

        # Resetting the marker of all the vertices
        self.reset_marker()
        #print(allowed_colors)

        # Altering the color of the first random node
        self.vertices[random_node.name].color = self.alter_color(allowed_colors, random_node.color)

        # Push the first random node to the Q
        q.put(self.vertices[random_node.name])

        # Start the queue processing loop
        while not q.empty():
            # Getting the first node
            node = q.get()

            # looping through the neigbors to see if any of the colors
            # conflict with the node. If color conflict, then we alternate it's color and
            # push the node to the Q. After that we check mark the main 'node' in Question
            # as it's processing is finished
            for neighbor in node.neighbors:
                if self.vertices[neighbor.name].checked == False:
                    if self.vertices[neighbor.name].color == self.vertices[node.name].color:
                        self.vertices[neighbor.name].color = self.alter_color(allowed_colors, self.vertices[neighbor.name].color)
                        q.put(self.vertices[neighbor.name])
            
            self.vertices[node.name].checked = True

        print('Kempe operator applied to the graph')

    def print_allnode_colors(self):
        for index, node in self.vertices.items():
            print(f'node = {index} ---> color = {node.color}')

    def print_all_neighbor_color(self):
        for index, node in self.vertices.items():
            print(f'node = {node.name} ---> color = {node.color}')
            print(f'Neighbor colors: {node.print_neighbor_color()}')

    def cal_avg_penalty(self,f):
        penalty_per_student = []
        
        # we traverse the file
        for row in f:
            temp_sum = 0
            slots = []
            courses = row.split()

            for index, course in enumerate(courses):
                slots.append(self.vertices[course].color)
            
            # sorting the slots from low to high
            slots.sort()
            # now we traverse the array pair wise and then we calculate the penalty
            for i in range(len(slots)-1):
                exam_gap = slots[i+1] - slots[i]
                if 1 <= exam_gap <= 5:
                    temp_sum += 2 ** (5-exam_gap)
                else:
                    temp_sum += 0
            penalty_per_student.append(temp_sum)

        self.avg_penalty = self.get_avg(penalty_per_student)

    @staticmethod
    def get_avg(penalty_list):
        return  sum(penalty_list)/len(penalty_list)



    def print_result(self,f):
        print(f'Dataset: {f.name} TimeSlot: {self.colors_needed} Penalty: {self.avg_penalty}')




            

    


    
