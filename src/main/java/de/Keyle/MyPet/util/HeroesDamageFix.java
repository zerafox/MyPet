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

package de.Keyle.MyPet.util;

import com.herocraftonline.heroes.Heroes;
import de.Keyle.MyPet.util.logger.DebugLogger;
import org.bukkit.entity.EntityType;

import java.lang.reflect.Method;

public class HeroesDamageFix
{
    private static Heroes heroesPlugin = null;
    private static boolean heroesSearched = false;

    private static void findHeroesPlugin()
    {
        if (MyPetBukkitUtil.getServer().getPluginManager().isPluginEnabled("Heroes"))
        {
            heroesPlugin = (Heroes) MyPetBukkitUtil.getServer().getPluginManager().getPlugin("Heroes");
        }
        heroesSearched = true;
        DebugLogger.info("HeroesDamageFix " + (heroesPlugin != null ? "" : "not ") + "activated.");
    }

    public static void reset()
    {
        heroesPlugin = null;
        heroesSearched = false;
    }

    public static boolean damageFaked(int damage)
    {
        if (!heroesSearched)
        {
            findHeroesPlugin();
        }
        if (heroesPlugin != null)
        {
            Object entityDamageObject;
            try
            {
                Method m = heroesPlugin.getDamageManager().getClass().getDeclaredMethod("getEntityDamage", EntityType.class);
                m.setAccessible(true);
                entityDamageObject = m.invoke(heroesPlugin.getDamageManager(), EntityType.UNKNOWN);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return false;
            }
            if (entityDamageObject != null)
            {
                int entityDamage = (Integer) entityDamageObject;
                if (entityDamage != damage)
                {
                    return true;
                }
            }
        }
        return false;
    }
}