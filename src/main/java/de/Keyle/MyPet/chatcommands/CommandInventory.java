/*
 * This file is part of MyPet
 *
 * Copyright (C) 2011-2013 Keyle
 * MyPet is licensed under the GNU Lesser General Public License.
 *
 * MyPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MyPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.Keyle.MyPet.chatcommands;

import de.Keyle.MyPet.entity.types.MyPet;
import de.Keyle.MyPet.entity.types.MyPet.PetState;
import de.Keyle.MyPet.skill.ISkillActive;
import de.Keyle.MyPet.skill.skills.implementation.Inventory;
import de.Keyle.MyPet.util.MyPetBukkitUtil;
import de.Keyle.MyPet.util.MyPetLanguage;
import de.Keyle.MyPet.util.MyPetList;
import de.Keyle.MyPet.util.MyPetPermissions;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandInventory implements CommandExecutor, TabCompleter
{
    private static List<String> emptyList = new ArrayList<String>();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            if (args.length == 0)
            {
                if (MyPetList.hasMyPet(player))
                {
                    MyPet myPet = MyPetList.getMyPet(player);
                    if (myPet.getStatus() == PetState.Despawned)
                    {
                        sender.sendMessage(MyPetBukkitUtil.setColors(MyPetLanguage.getString("Msg_CallFirst")).replace("%petname%", myPet.petName));
                        return true;
                    }
                    if (myPet.getStatus() == PetState.Dead)
                    {
                        sender.sendMessage(MyPetBukkitUtil.setColors(MyPetLanguage.getString("Msg_CallDead")).replace("%petname%", myPet.petName).replace("%time%", "" + myPet.respawnTime));
                        return true;
                    }
                    if (player.getGameMode() == GameMode.CREATIVE && !MyPetPermissions.has(player, "MyPet.admin", false))
                    {
                        sender.sendMessage(MyPetBukkitUtil.setColors(MyPetLanguage.getString("Msg_InventoryCreative")).replace("%petname%", myPet.petName));
                        return true;
                    }
                    if (!MyPetPermissions.hasExtended(player, "MyPet.user.extended.Inventory") && !MyPetPermissions.has(player, "MyPet.admin", false))
                    {
                        myPet.sendMessageToOwner(MyPetBukkitUtil.setColors(MyPetLanguage.getString("Msg_CantUse")));
                        return true;
                    }
                    if (myPet.getSkills().hasSkill("Inventory"))
                    {
                        ((ISkillActive) myPet.getSkills().getSkill("Inventory")).activate();
                    }
                }
                else
                {
                    sender.sendMessage(MyPetBukkitUtil.setColors(MyPetLanguage.getString("Msg_DontHavePet")));
                }
            }
            else if (args.length == 1 && MyPetPermissions.has(player, "MyPet.admin", false))
            {
                Player petOwner = MyPetBukkitUtil.getServer().getPlayer(args[0]);

                if (petOwner == null || !petOwner.isOnline())
                {
                    sender.sendMessage(MyPetBukkitUtil.setColors(MyPetLanguage.getString("Msg_PlayerNotOnline")));
                }
                else if (MyPetList.hasMyPet(petOwner))
                {
                    MyPet myPet = MyPetList.getMyPet(petOwner);
                    if (myPet.getSkills().isSkillActive("Inventory"))
                    {
                        ((Inventory) myPet.getSkills().getSkill("Inventory")).openInventory(player);
                    }
                }
            }
            return true;
        }
        sender.sendMessage("You can't use this command from server console!");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if(strings.length == 1 && MyPetPermissions.has((Player) commandSender,"MyPet.admin",false))
        {
            return null;
        }
        return emptyList;
    }
}