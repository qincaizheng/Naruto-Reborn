package com.shadow_ninja.narutoreborn.command;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.shadow_ninja.narutoreborn.data.NinjaData;
import com.shadow_ninja.narutoreborn.data.NinjaDataHandler;
import com.shadow_ninja.narutoreborn.ninjutsu.Ininjutsu;
import com.shadow_ninja.narutoreborn.ninjutsu.NinjutsuContainer;
import com.shadow_ninja.narutoreborn.ninjutsu.NinjutsuRegistry;
import com.shadow_ninja.narutoreborn.ninjutsu.NinjutsuUseService;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public final class NinjutsuCommands {
    private NinjutsuCommands() {
    }

    private static final SuggestionProvider<CommandSourceStack> NINJUTSU_SUGGESTIONS = (ctx, builder) -> SharedSuggestionProvider.suggestResource(NinjutsuRegistry.ids(), builder);

    public static void register(RegisterCommandsEvent event) {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("ninjutsu")
                .then(Commands.literal("use")
                        .then(Commands.argument("name", ResourceLocationArgument.id()).suggests(NINJUTSU_SUGGESTIONS)
                                .then(Commands.argument("power", DoubleArgumentType.doubleArg())
                                        .executes(NinjutsuCommands::executeUse))))
                .then(Commands.literal("learn")
                        .then(Commands.argument("name", ResourceLocationArgument.id()).suggests(NINJUTSU_SUGGESTIONS)
                                .executes(NinjutsuCommands::executeLearn)))
                .then(Commands.literal("forget")
                        .then(Commands.argument("name", ResourceLocationArgument.id()).suggests(NINJUTSU_SUGGESTIONS)
                                .executes(NinjutsuCommands::executeForget)))
                .then(Commands.literal("unlock")
                        .then(Commands.argument("name", ResourceLocationArgument.id()).suggests(NINJUTSU_SUGGESTIONS)
                                .executes(ctx -> executeToggleFlag(ctx, true, true))))
                .then(Commands.literal("lock")
                        .then(Commands.argument("name", ResourceLocationArgument.id()).suggests(NINJUTSU_SUGGESTIONS)
                                .executes(ctx -> executeToggleFlag(ctx, true, false))))
                .then(Commands.literal("enable")
                        .then(Commands.argument("name", ResourceLocationArgument.id()).suggests(NINJUTSU_SUGGESTIONS)
                                .executes(ctx -> executeToggleFlag(ctx, false, true))))
                .then(Commands.literal("disable")
                        .then(Commands.argument("name", ResourceLocationArgument.id()).suggests(NINJUTSU_SUGGESTIONS)
                                .executes(ctx -> executeToggleFlag(ctx, false, false))))
                .then(Commands.literal("quickbar")
                        .then(Commands.argument("name", ResourceLocationArgument.id()).suggests(NINJUTSU_SUGGESTIONS)
                                .executes(NinjutsuCommands::executeQuickbarAdd)))
                .then(Commands.literal("remove")
                        .then(Commands.argument("name", ResourceLocationArgument.id()).suggests(NINJUTSU_SUGGESTIONS)
                                .executes(NinjutsuCommands::executeQuickbarRemove)));

        LiteralArgumentBuilder<CommandSourceStack> dataRoot = Commands.literal("narutoreborn")
                .requires(src -> src.hasPermission(2))
                .then(Commands.literal("level")
                        .then(Commands.argument("value", IntegerArgumentType.integer(0))
                                .executes(NinjutsuCommands::executeSetLevel)))
                .then(Commands.literal("chakra")
                        .then(Commands.argument("value", DoubleArgumentType.doubleArg(0))
                                .executes(NinjutsuCommands::executeSetChakra)))
                .then(Commands.literal("maxchakra")
                        .then(Commands.argument("value", DoubleArgumentType.doubleArg(0))
                                .executes(NinjutsuCommands::executeSetMaxChakra)))
                .then(Commands.literal("experience")
                        .then(Commands.argument("value", IntegerArgumentType.integer(0))
                                .executes(NinjutsuCommands::executeSetExperience)))
                .then(Commands.literal("reset")
                        .executes(NinjutsuCommands::executeReset));

        event.getDispatcher().register(root);
        event.getDispatcher().register(dataRoot);
    }

    private static Optional<ServerPlayer> requirePlayer(CommandSourceStack source) {
        if (source.getEntity() instanceof ServerPlayer player) {
            return Optional.of(player);
        }
        source.sendFailure(Component.translatable("command.narutoreborn.ninjutsu.player_only"));
        return Optional.empty();
    }

    private static Optional<NinjutsuContainer> requireOwnedContainer(ServerPlayer player, ResourceLocation id, CommandSourceStack source) {
        NinjaData data = NinjaDataHandler.instance().get(player);
        Optional<NinjutsuContainer> opt = data.findContainer(id);
        if (opt.isEmpty()) {
            source.sendFailure(Component.translatable("message.narutoreborn.ninjutsu.not_owned"));
        }
        return opt;
    }

    private static int executeUse(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();
        Optional<ServerPlayer> maybePlayer = requirePlayer(source);
        if (maybePlayer.isEmpty()) {
            return 0;
        }
        ServerPlayer player = maybePlayer.get();

        double power = DoubleArgumentType.getDouble(ctx, "power");
        ResourceLocation id = ResourceLocationArgument.getId(ctx, "name");
        Ininjutsu ninjutsu = NinjutsuRegistry.get(id).orElse(null);
        if (ninjutsu == null) {
            source.sendFailure(Component.translatable("message.narutoreborn.ninjutsu.unknown", id.toString()));
            return 0;
        }
        double clamped = Math.max(ninjutsu.getMinPower(), Math.min(ninjutsu.getMaxPower(), power));

        var result = NinjutsuUseService.tryUse(player, id, clamped);
        if (!result.success()) {
            source.sendFailure(result.message());
            return 0;
        }
        source.sendSuccess(result::message, true);
        return 1;
    }

    private static int executeLearn(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();
        Optional<ServerPlayer> maybePlayer = requirePlayer(source);
        if (maybePlayer.isEmpty()) {
            return 0;
        }
        ServerPlayer player = maybePlayer.get();
        ResourceLocation id = ResourceLocationArgument.getId(ctx, "name");
        if (NinjutsuRegistry.get(id).isEmpty()) {
            source.sendFailure(Component.translatable("message.narutoreborn.ninjutsu.unknown", id.toString()));
            return 0;
        }
        boolean learned = NinjaDataHandler.instance().learnNinjutsu(player, id);
        if (!learned) {
            source.sendFailure(Component.translatable("message.narutoreborn.ninjutsu.learned"));
            return 0;
        }
        // add to quickbar if not present
        NinjaData data = NinjaDataHandler.instance().get(player);
        if (!data.getQuickbarIds().contains(id)) {
            data.getQuickbarIds().add(id);
            NinjaDataHandler.instance().save(player, data);
        }
        source.sendSuccess(() -> Component.translatable("message.narutoreborn.ninjutsu.learn", id.toString()), true);
        return 1;
    }

    private static int executeForget(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();
        Optional<ServerPlayer> maybePlayer = requirePlayer(source);
        if (maybePlayer.isEmpty()) {
            return 0;
        }
        ServerPlayer player = maybePlayer.get();
        ResourceLocation id = ResourceLocationArgument.getId(ctx, "name");
        NinjaData data = NinjaDataHandler.instance().get(player);
        boolean removed = data.getNinjutsuContainers().removeIf(c -> c.id().equals(id));
        if (!removed) {
            source.sendFailure(Component.translatable("message.narutoreborn.ninjutsu.not_owned"));
            return 0;
        }
        data.getQuickbarIds().removeIf(id::equals);
        NinjaDataHandler.instance().save(player, data);
        source.sendSuccess(() -> Component.translatable("message.narutoreborn.ninjutsu.forget", id.toString()), true);
        return 1;
    }

    private static int executeToggleFlag(CommandContext<CommandSourceStack> ctx, boolean unlockFlag, boolean value) {
        CommandSourceStack source = ctx.getSource();
        Optional<ServerPlayer> maybePlayer = requirePlayer(source);
        if (maybePlayer.isEmpty()) {
            return 0;
        }
        ServerPlayer player = maybePlayer.get();
        ResourceLocation id = ResourceLocationArgument.getId(ctx, "name");
        Optional<NinjutsuContainer> opt = requireOwnedContainer(player, id, source);
        if (opt.isEmpty()) {
            return 0;
        }
        NinjutsuContainer container = opt.get();
        if (unlockFlag) {
            container.setUnlocked(value);
        } else {
            container.setEnabled(value);
        }
        NinjaDataHandler.instance().save(player, NinjaDataHandler.instance().get(player));
        source.sendSuccess(() -> Component.translatable("message.narutoreborn.ninjutsu.updated", id.toString()), true);
        return 1;
    }

    private static int executeQuickbarAdd(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();
        Optional<ServerPlayer> maybePlayer = requirePlayer(source);
        if (maybePlayer.isEmpty()) {
            return 0;
        }
        ServerPlayer player = maybePlayer.get();
        ResourceLocation id = ResourceLocationArgument.getId(ctx, "name");
        Optional<NinjutsuContainer> opt = requireOwnedContainer(player, id, source);
        if (opt.isEmpty()) {
            return 0;
        }
        NinjaData data = NinjaDataHandler.instance().get(player);
        if (!data.getQuickbarIds().contains(id)) {
            data.getQuickbarIds().add(id);
            NinjaDataHandler.instance().save(player, data);
        }
        source.sendSuccess(() -> Component.translatable("message.narutoreborn.ninjutsu.quickbar.add", id.toString()), true);
        return 1;
    }

    private static int executeQuickbarRemove(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();
        Optional<ServerPlayer> maybePlayer = requirePlayer(source);
        if (maybePlayer.isEmpty()) {
            return 0;
        }
        ServerPlayer player = maybePlayer.get();
        ResourceLocation id = ResourceLocationArgument.getId(ctx, "name");
        NinjaData data = NinjaDataHandler.instance().get(player);
        boolean removed = data.getQuickbarIds().removeIf(id::equals);
        if (!removed) {
            source.sendFailure(Component.translatable("message.narutoreborn.quickbar.empty"));
            return 0;
        }
        NinjaDataHandler.instance().save(player, data);
        source.sendSuccess(() -> Component.translatable("message.narutoreborn.ninjutsu.quickbar.remove", id.toString()), true);
        return 1;
    }

    private static boolean requirePermission(CommandSourceStack source) {
        if (!source.hasPermission(2)) {
            source.sendFailure(Component.translatable("message.narutoreborn.permission_denied"));
            return false;
        }
        return true;
    }

    private static int executeSetLevel(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();
        if (!requirePermission(source)) return 0;
        Optional<ServerPlayer> maybePlayer = requirePlayer(source);
        if (maybePlayer.isEmpty()) return 0;
        ServerPlayer player = maybePlayer.get();
        int level = IntegerArgumentType.getInteger(ctx, "value");
        NinjaDataHandler.instance().setLevel(player, level);
        source.sendSuccess(() -> Component.translatable("message.narutoreborn.data.level", level), true);
        return 1;
    }

    private static int executeSetChakra(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();
        if (!requirePermission(source)) return 0;
        Optional<ServerPlayer> maybePlayer = requirePlayer(source);
        if (maybePlayer.isEmpty()) return 0;
        ServerPlayer player = maybePlayer.get();
        double value = DoubleArgumentType.getDouble(ctx, "value");
        NinjaDataHandler.instance().setChakraCurrent(player, value);
        source.sendSuccess(() -> Component.translatable("message.narutoreborn.data.chakra", value), true);
        return 1;
    }

    private static int executeSetMaxChakra(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();
        if (!requirePermission(source)) return 0;
        Optional<ServerPlayer> maybePlayer = requirePlayer(source);
        if (maybePlayer.isEmpty()) return 0;
        ServerPlayer player = maybePlayer.get();
        double value = DoubleArgumentType.getDouble(ctx, "value");
        NinjaDataHandler.instance().setChakraMax(player, value);
        source.sendSuccess(() -> Component.translatable("message.narutoreborn.data.maxchakra", value), true);
        return 1;
    }

    private static int executeSetExperience(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();
        if (!requirePermission(source)) return 0;
        Optional<ServerPlayer> maybePlayer = requirePlayer(source);
        if (maybePlayer.isEmpty()) return 0;
        ServerPlayer player = maybePlayer.get();
        int value = IntegerArgumentType.getInteger(ctx, "value");
        NinjaDataHandler.instance().setExperience(player, value);
        source.sendSuccess(() -> Component.translatable("message.narutoreborn.data.experience", value), true);
        return 1;
    }

    private static int executeReset(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();
        if (!requirePermission(source)) return 0;
        Optional<ServerPlayer> maybePlayer = requirePlayer(source);
        if (maybePlayer.isEmpty()) return 0;
        ServerPlayer player = maybePlayer.get();
        NinjaData fresh = new NinjaData();
        NinjaDataHandler.instance().save(player, fresh);
        source.sendSuccess(() -> Component.translatable("message.narutoreborn.data.reset"), true);
        return 1;
    }
}
