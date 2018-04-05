		Design Document
		Tomb of the Forgotten Memories
		Canet - Chourouq

# Design

## 1 Representation of a Room

A room is represented by:

 - A description
 - Items that can be found when searching
 - Items in chests
 - Notes
 - Its neighbors
 - (Enemies are found in rooms but are not part of the rooms; see \[Enemy\])

Let's detail all of these. (Note: the room does NOT know its own position in the World, because it's unneeded).

##### Description

The room's description is a String that is printed to the GUI when the user walks into the room. It can not change, and must be internationalized (thanks to the Translator platform).

##### Searching a Room

A player can use one of its skills to search a Room. A room can only be looted once, which means that the Items do not need to be stored in the Room object (they are generated on the fly when the user searches the room); therefore the Room only stores whether it was already looted or not.

##### Items in containers

A room can contain a container (read, a chest) that has different items in it. Multiple types of containers exists but they are only differentiated by their description and their capacity.

##### Notes

Notes are story elements, they only consist of a description and, optionally, an author. The user can find them in rooms, but cannot take them with him. Notes are generated from a list of notes loaded from a file (this allows to easily change the story elements without needing to recompile).

##### The neighbors

The neighbors of a Room are stored as a Map of Direction and Room, that is, any given Room associates directions where it has a neighbor with that neighbor. This way, you can easily:

 - Check if a Room has a neighbor in a specific direction (thanks to `.containsKey`)
 - Get the neighboring Room in that direction (thanks to `.get`)

However, this is not the only way to access a Room, as they can be found through the World object, that knows the location of every room.

## 2 Input

The user will input commands through button pressing.

The GUI is split into 4 parts;

 - The Player (left side), contains information about the player (health points...)
 - The Story (top), contains the data from the game (the enemy's stats, the description of the room...)
 - The Actions (bottom center), contains the buttons to interact with the game
 - The Map (bottom right), contains a 2D-representation of the floor the player is in. In our case, the Map representation is very important to the gameplay, as it is the only way the player can know if enemies are awaiting him. In a first time, we will implement the map in ASCII-art (alike what the game A Dark Room did), and, if possible, we will change it to a Map drawn with squares and circles at the of the project.

## 3

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

## 4 Setting

The game is set in a medieval-like fantasy-world. Because there are no graphics, however, this will only be noticeable through the description of rooms, items, and through the story notes - as with the different item types.

There are three item types: Physicals, Magicals, and Unique Use.

Physicals represent the "normal" objects - armor, weapons... They are separated into multiple types:
 - Armors are used to protect yourself from your enemies
 - Weapons are used to attack enemies using your stamina: the stronger you are, the more damage you inflict upon your enemies - and vice-versa, the weaker you are, the less damage you inflict.

Magical items are mainly weapons: they are used to attack your enemies by consuming mana (mana increases overtime). Unlike physical items, they do not alter your damage based on the quantity of mana left (nor your stamina, for that matter). This makes magical items the counterpart of physical items, they each have their pros and cons, so you should probably cary both kind with you.

Unique Use items are similar as Physical and Magical weapons, because they can consume mana or reduce your stamina - what makes them special is that they cannot be used twice (that is, they are cleared from your inventory on usage). They can go from spells (inflict damage on your opponent) to potions (regenerate your health points, improve your stamina), explosives... In the code though, they only differ by their description and their effect.

## 5 Character Attributes

### Entity attributes

The characters (called "entities", see glossary) have several attributes:

 - Health Points (HP): Can be regenerated using potions (for example). When HP reaches 0, the entity dies (read, is removed from the World)
 - Stamina: Decreases when the entity moves, fights... Can be replenished through sleeping
 - Mana: The mana the entity is able to use to power magical items

An entity also has:

 - A position (Location object)

### Player attributes

The player has several attributes that a regular Entity doesn't have:

 - An inventory (a container of items, as seen in a Room)
 - The search luck: is improved overtime during playing
 - The enemy: last targetted enemy (none if none was targetted or if it was defeated)
 - The weight he can carry (used to choose whether the player can take a new Item in their inventory)

## 6 Optional Features

 - Representation of the map in 2D
 - Different AIs
 - Different enemy types (kamikazes...)
 - Loading JSON through reflexivity (see File/Database)
 - AIs can cooperate (one AI 'drives' multiple entities)
 - Allow AIs to join a fight (1v* fights)

## 7 Use-Case scenario

Plain-english scenario of the game:

 - The user starts the game
 - In the general menu, the user chooses to load a save file
 - The user is prompted to choose the file
 - The file is loaded and the story is displayed in the state it was left in
 - The user changes room (dir. North)
 - The user chooses to use an item
 - The item is used
 - The user saves the game
 - The user exits the game

# 2 File/Database of the System

The save files will be stored using the JSON format.

In a first version, the objects will be loaded using a traditional Builder object, and saved with the same object.

In a second version, we'd like to be able to save/load the objects using annotations; the loader would see what the class 'wants' and assign it. This is an optional feature, as we do not know if we will be able to do it.

# 3 Events

## External events

Let's order the external events by the context in which they can be sent.

### Exploration

 - ARR_Move(): change room
 - ARR_Rest(): Accelerates the game speed (the player rests to refill their stamina bar, however this can lead to enemies breaking in)
 - ARR_StopRest(): (only when resting) Stops resting, resets the game speed
 - ARR_Search(): (only if the room has not been searched) The user searches the room he's in to find items
 - ARR_OpenChest(): (only if chest exists) The user opens the chest of the room

### Fighting

Fighting mode is automatically started when the player is in the same room as any other Entity. It can either end with ARR_Flee() or with the death of either the player or the enemy. On the enemy's death, the player's inventory is openned so the player can take the loot.

 - ARR_Flee(): Similar to ARR\_Move(), but consumes more stamina and can fail. On success, stops the fighting mode
 - ARR_Attack(): Opens the inventory and prompts for an item, then attacks using that item

### In inventory

The player can always open their inventory.

 - ARR_CloseInventory()
 - ARR_ThrowItem(): throws an item
 - ARR_TakeItem(): if comparing inventory with a container, takes an item from the container and puts it into the player's inventory
 - ARR_SelectItem(): selects an item and closes the inventory

## Time events

Even though there are actions that happen "overtime" (like regeneration...), there are no time events: whenever the player does an action (note that I said action and not interaction), every object is notified that time has passed; the only exception being "restmode", in which the player doesn't act anymore. In RestMode, there exists:

 - TIME_NextTick(): Updates every objects (entities move, stamina bar replenishes...), the player cannot do anything except ARR\_StopRest()

## Result events

 - OUT_Refresh(): updates the screen
 - OUT_SelectInInventory(): allows the player to act on its inventory
