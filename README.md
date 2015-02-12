# BattleCode 2015 #

## Team Ayyyyyyyylmao ##

This is Team Ayyyyyyyylmao's Battlecode 2015 submission. The final bot can be viewed in the "team170" folder.

## Notes: ##

- Due to the fast iteration rate, some of the commit messages are half assed or incoherent. Oops.
- Please ignore some of the spaghetti code. Due to the fast iteration rate, we didn't have time to refactor anything. Oops x2.

## Final Results: ##

- Final Tournament: 12-16
- Open Tournament: 7-8

## Strategy Report ##

**Beavers**

- All beavers should stay close to the HQ because their sole purpose is to build buildings. We didn’t find that beavers helped our economy at all when mining, and their high cost with low output made miners a much better choice for us. We only built as many beavers as necessary for our building output, and no more than that.

**Miners**

- When spawned, miners will survey the ore around them and rank it based on their distance to the ore, distance of the ore to the middle of the two bases (which we considered to be a danger zone), and the amount of ore that was actually there ((distanceToMiddle * ore^3)/distanceToMiner). When they find a good ore location, they’ll just head toward it and survey a small radius around them just to make sure they didn’t miss any better ore spots. 
- Initially when we made the miners go to the best ore, they wouldn’t rank it based on distance. This caused us to have the greediest miners ever. They literally would run into the middle of the map without caring about anything around them and mine. Although we appreciated the miners’ effort, this caused our economy to suffer when teams would do any type of early aggression.
Miners will also broadcast the largest amount of ore that they’ve seen so other miners can head toward ore rich spots and reap the rewards.

**Drones**

- In the first tournament, we used drones to do some minor economy harassment. In the qualifying tournament and above, we only build one drone. Its sole purpose is to deliver supply to other units. 
- Whenever our cute little single supply drone receives more supply from the HQ, it will survey all of the units, clump them into groups, and decide on which groups have the most priority (this uses tens of thousands of bytecodes but oh well yolo). From there it’ll make its supply run until it is left with 500 bytecodes, at which time it’ll return home to the HQ for more.

**Launchers**

- Launchers will initially hold out at the closest tower to the middle of the two HQs until there are more than 6 of them. At this point, they will mobilize and move out.
- When attacking, launchers will maintain a root(35) square radius away from enemy units. This allows them to spot other units but will keep them relatively safe as well.
- When firing a missile, the launcher will find the direction of the best enemy (determined by the lowest HP) and will set this direction integer in the broadcast array.
- When mobilizing, launchers will head to the closest objective (an objective is either a Tower or an HQ). This allows us to take towers fast and do multi-pronged attacks, instead of having just one large clump of launchers.

**Missiles**

- For their first turn, missiles will follow the direction set out for them in the broadcast array.
- After the first turn, missiles will find the best enemy (prioritizing towers and the enemy HQ) and head towards that. If they’re within exploding range, they’ll detonate. Our missiles will ignore other missiles completely, as we had some nasty cases of friendly fire otherwise.
