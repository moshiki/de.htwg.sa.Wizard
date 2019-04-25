val normals = for {
  color <- List("red", "blue", "yellow", "green")
  number <- 1 to 13
} yield DefaultCard(color, number)