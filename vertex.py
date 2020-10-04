class Vertex:
    def __init__(self, name):
        self.name = name
        self.color = -1
        self.blame = -1
        self.checked = False
        self.sat_degree = 0
        self.neighbors = []

    def add_neighbor_directed(self, v):
        if isinstance(v, Vertex):
            if v not in self.neighbors:
                self.neighbors.append(v)
                #self.neighbors.sort(key=lambda x: x.name)
                return True
        else:
            return False

    def add_neighbor_undirected(self, neighbor):
        if isinstance(neighbor, Vertex):
            if neighbor not in self.neighbors:
                self.neighbors.append(neighbor)
                neighbor.neighbors.append(self)
                #self.neighbors.sort(key=lambda x: x.name)
                #neighbor.neighbors.sort(key=lambda x: x.name)
        else:
            return False

    def add_neighbors_undirected(self, neighbors):
        for neighbor in neighbors:
            if isinstance(neighbor, Vertex):
                if neighbor not in self.neighbors:
                    self.neighbors.append(neighbor)
                    neighbor.neighbors.append(self)
                    #self.neighbors.sort(key=lambda x: x.name)
                    #neighbor.neighbors.sort(key=lambda x: x.name)
            else:
                return False

    def get_vertex_degree(self):
        return len(self.neighbors)

    def __repr__(self):
        neighbors = [neighbor.name for neighbor in self.neighbors]
        return f'name: {self.name} neighbors: {neighbors}'

    def print_neighbor_color(self):
        neighbor_name = [neighbor.name for neighbor in self.neighbors]
        neighbors = [neighbor.color for neighbor in self.neighbors]
        print(neighbor_name)
        print(neighbors)

    def get_saturation_degree(self):
        unique_color = set()
        for neighbor in self.neighbors:
            if neighbor.color != -1:
                unique_color.add(neighbor.color)

        self.sat_degree = len(unique_color)
        
        return self.sat_degree

    def set_saturation_degree(self):
        unique_color = set()
        for neighbor in self.neighbors:
            if neighbor.color != -1:
                unique_color.add(neighbor.color)

        self.sat_degree = len(unique_color)


    

    