package com.shadow_ninja.narutoreborn.entity.katon;

import com.shadow_ninja.narutoreborn.entity.EntityInit;
import com.shadow_ninja.narutoreborn.ninjutsu.katon.FireballJutsu;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

import java.util.List;

/**
 * Katon fireball projectile.
 *
 * Contract per README:
 * - Explosion particles but NO block destruction.
 * - Area damage + ignite enemies.
 */
public class KatonFireballEntity extends ThrowableProjectile {
    private final float radius;
    private final float damage;
    private final int fireSeconds;
    private final double range;

    public KatonFireballEntity(EntityType<? extends ThrowableProjectile> type, Level level) {
        super(type, level);
        this.radius = 3.0f;
        this.damage = 6.0f;
        this.fireSeconds = 4;
        this.range = 24.0d;
    }

    public KatonFireballEntity(ServerLevel level, LivingEntity owner,double range, float radius, float damage, int fireSeconds) {
        super(EntityInit.KATON_FIREBALL.get(), level);
        this.setOwner(owner);
        this.setPos(owner.getX(), owner.getEyeY() - 0.1, owner.getZ());
        this.radius = radius;
        this.damage = damage;
        this.fireSeconds = fireSeconds;
        this.range = range;
    }

    @Override
    protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
        // no extra data
    }

    public double getRadius() {
        return radius;
    }

    @Override
    protected double getDefaultGravity() {
        return 0.0F;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            if (this.getOwner() == null) {
                discard();
                return;
            }
            // simple lifetime
            if (this.distanceTo(this.getOwner()) > this.range) {
                discard();
            }
        } else {
            // trail particles
            this.level().addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (this.level().isClientSide) {
            return;
        }

        if (!(this.getOwner() instanceof LivingEntity owner)) {
            discard();
            return;
        }

        ServerLevel server = (ServerLevel) this.level();

        // Explosion-like particles only.
        server.sendParticles(ParticleTypes.EXPLOSION,
                this.getX(), this.getY(), this.getZ(),
                1, 0, 0, 0, 0);
        server.sendParticles(ParticleTypes.FLAME,
                this.getX(), this.getY(), this.getZ(),
                80, radius * 0.15, radius * 0.15, radius * 0.15, 0.03);

        // Apply AoE effects.
        var aabb = this.getBoundingBox().inflate(radius);
        List<Entity> entities = this.level().getEntities(this, aabb);
        DamageSource src = this.damageSources().thrown(this, owner);

        for (Entity e : entities) {
            if (!FireballJutsu.canAffect(e, owner)) {
                continue;
            }
            e.hurt(src, damage);
            int ticks = fireSeconds * 20;
            // 1.21.1: ignite via remaining fire ticks
            e.setRemainingFireTicks(Math.max(e.getRemainingFireTicks(), ticks));
        }

        discard();
    }
}
