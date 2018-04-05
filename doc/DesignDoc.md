		Design Document
		Tomb of the Forgotten
		Canet - Chourouq

# 1.3

Several commands are necessary to play the game. They are implemented into different fields :

- REST - The character gets some sleep and gains more energy in order to restore some of his abilities (strength). Accelerate the AIs speeds for a few turns. The character cannot do anything else while resting.
- STOP_REST - The character stops resting and can carry on.

- EXPLORE - Explore the room he's in only once, to collect new items and select which of them he wants to keep.
- STOP_EXPLORE - Stop exploring the room.

[INVENTORY]
- OPEN/CLOSE - Open the inventory to view each item owned, their type, the description, their ability and their amount of pods.
- THROW/DROP - Throw away an item from the inventory resulting in lowering pods and increasing the loot in the room.
- GRAB : Pick up an item found in a room and choose whether or not it needs to be kept. 
- EQUIP_ITEM : Equip an item from the inventory.
- USE_ITEM : Use an item. May be done during a fight.
	> DRINK_POTION : Drink a potion. May be done during a fight.
- CHOOSE-ITEM : Select an item from the inventory. May be done while fighting.

[ROOM]
- OPEN/CLOSE DOOR - Close door.
- MOVE (NORTH/SOUTH/EAST/WEST) : Move to a direction.
- SEARCH : Search room to find items. Some hidden items may be found depending on the player's search luck.
- READ_NOTE : Read a note. 

[COMBAT]
- FLEE : Escape a fight.
- ATTACK : Attack the ennemy.
- SEARCH_BODY : Search body of defeated AIs.
- USE_ITEM : Use an item. May be done during a fight.
	> USE_PARCHMENT : One parchment = One spell. It's a one-used only item. It can be 	used while fighting to deal damage to the ennemies.


