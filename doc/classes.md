		List of the classes
		Tomb of the Forgotten Memories
		Canet - Chourouq

Container:
 - Has items
 - Has a description (="chest", ...)
 - addItem()
 - transferTo(Container, Item)

Room:
 - Has neighbors
 - Can have a container
 - Has a description
 - Knows if it has been looted, can generate items once
 - Can have notes

Note:
 - Has an ID (from the file)
 - Has a description
 - Can have an author (or be anonymous); the author is a String

Direction enum:
 - Used to move from one Location to an other

Location:
 - X, Y, Z

Entity:
 - Health
 - Stamina: decreased by moving, fleeing, fighting...; increased by resting
 - Mana: descreased by magic use, increased overtime

World:
 - getReachableRooms(Predicate<Location>): TreeMap of Location, Room
 - getReachableRooms(int): TreeMap of Location, Room
 - getPathTo(Room): Optional<Path>
 - getPathTo(Room, int): Optional<Path>
 - getPathTo(Room, Predicate<Location>): Optional<Path>

Builder<T, R> interface:
 - write(T, R)
 - open(T, R)

TODO LIST
 - Talk about "action per tick" and not "action per second"
 - Glossary: character -> entity
