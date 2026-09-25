package squeek.appleskin.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record NaturalRegenerationSyncPayload(boolean naturalRegeneration) implements CustomPacketPayload {
	public static final Type<NaturalRegenerationSyncPayload> TYPE =
			new Type<>(Identifier.fromNamespaceAndPath("appleskin", "natural_regeneration_sync"));

	public static final StreamCodec<RegistryFriendlyByteBuf, NaturalRegenerationSyncPayload> CODEC =
			StreamCodec.composite(
					ByteBufCodecs.BOOL,
					NaturalRegenerationSyncPayload::naturalRegeneration,
					NaturalRegenerationSyncPayload::new
			);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
