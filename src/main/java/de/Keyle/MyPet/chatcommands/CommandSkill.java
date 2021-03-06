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
import de.Keyle.MyPet.skill.skills.implementation.ISkillInstance;
import de.Keyle.MyPet.util.MyPetBukkitUtil;
import de.Keyle.MyPet.util.MyPetLanguage;
import de.Keyle.MyPet.util.MyPetList;
import de.Keyle.MyPet.util.MyPetPermissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandSkill implements CommandExecutor, TabCompleter
{
    private static List<String> emptyList = new ArrayList<String>();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player petOwner = (Player) sender;
            if (args.length > 0 && MyPetPermissions.has(petOwner, "MyPet.admin", false))
            {
                petOwner = MyPetBukkitUtil.getServer().getPlayer(args[0]);

                if (petOwner == null || !petOwner.isOnline())
                {
                    sender.sendMessage(MyPetBukkitUtil.setColors(MyPetLanguage.getString("Msg_PlayerNotOnline")));
                    return true;
                }
                else if (!MyPetList.hasMyPet(petOwner))
                {
                    sender.sendMessage(MyPetBukkitUtil.setColors(MyPetLanguage.getString("Msg_UserDontHavePet").replace("%playername%", petOwner.getName())));
                    return true;
                }
            }

            if (MyPetList.hasMyPet(petOwner))
            {
                MyPet myPet = MyPetList.getMyPet(petOwner);
                sender.sendMessage(MyPetBukkitUtil.setColors(MyPetLanguage.getString("Msg_Skills")).replace("%petname%", myPet.petName).replace("%skilltree%", (myPet.getSkillTree() == null ? "None" : myPet.getSkillTree().getDisplayName())));

                for (ISkillInstance skill : myPet.getSkills().getSkills())
                {
                    if (skill.isActive())
                    {
                        sender.sendMessage(MyPetBukkitUtil.setColors("%green%%skillname%%white% " + skill.getFormattedValue()).replace("%skillname%", skill.getName()));
                    }
                }
                return true;
            }
            else
            {
                sender.sendMessage(MyPetBukkitUtil.setColors(MyPetLanguage.getString("Msg_DontHavePet")));
            }
            return true;
        }
        sender.sendMessage("You can't use this command from server console!");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if (strings.length == 1 && MyPetPermissions.has((Player) commandSender, "MyPet.admin", false))
        {
            return null;
        }
        return emptyList;
    }
}