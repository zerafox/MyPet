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

package de.Keyle.MyPet.entity.types.spider;

import de.Keyle.MyPet.entity.EntitySize;
import de.Keyle.MyPet.entity.types.EntityMyPet;
import de.Keyle.MyPet.entity.types.MyPet;
import net.minecraft.server.v1_5_R2.EnumMonsterType;
import net.minecraft.server.v1_5_R2.World;

@EntitySize(width = 1.4F, height = 0.9F)
public class EntityMySpider extends EntityMyPet
{
    public EntityMySpider(World world, MyPet myPet)
    {
        super(world, myPet);
        this.texture = "/mob/spider.png";
    }

    public EnumMonsterType getMonsterType()
    {
        return EnumMonsterType.ARTHROPOD;
    }

    // Obfuscated Methods -------------------------------------------------------------------------------------------

    protected void a()
    {
        super.a();
        this.datawatcher.a(16, new Byte((byte) 0)); // N/A
    }

    @Override
    protected void a(int i, int j, int k, int l)
    {
        makeSound("mob.spider.step", 0.15F, 1.0F);
    }

    /**
     * Returns the default sound of the MyPet
     */
    protected String bb()
    {
        return !playIdleSound() ? "" : "mob.spider.say";
    }

    /**
     * Returns the sound that is played when the MyPet get hurt
     */
    @Override
    protected String bc()
    {
        return "mob.spider.say";
    }

    /**
     * Returns the sound that is played when the MyPet dies
     */
    @Override
    protected String bd()
    {
        return "mob.spider.death";
    }

    public void a(boolean flag)
    {
        byte b0 = this.datawatcher.getByte(16);

        if (flag)
        {
            b0 = (byte) (b0 | 0x1);
        }
        else
        {
            b0 = (byte) (b0 & 0xFFFFFFFE);
        }
        this.datawatcher.watch(16, b0);
    }

    public void l_()
    {
        super.l_();
        if (!this.world.isStatic)
        {
            f(this.positionChanged);
        }
    }
}