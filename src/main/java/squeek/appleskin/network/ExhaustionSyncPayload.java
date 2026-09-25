package squeek.appleskin.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ExhaustionSyncPayload(float exhaustion) implements CustomPacketPayload {
	public static final Type<ExhaustionSyncPayload> TYPE =
			new Type<>(Identifier.fromNamespaceAndPath("appleskin", "exhaustion_sync"));

	public static final StreamCodec<RegistryFriendlyByteBuf, ExhaustionSyncPayload> CODEC =
			StreamCodec.composite(
					ByteBufCodecs.FLOAT,
					ExhaustionSyncPayload::exhaustion,
					ExhaustionSyncPayload::new
			);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
