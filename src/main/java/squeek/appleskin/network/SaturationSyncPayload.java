package squeek.appleskin.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record SaturationSyncPayload(float saturation) implements CustomPacketPayload {
	public static final Type<SaturationSyncPayload> TYPE =
			new Type<>(Identifier.fromNamespaceAndPath("appleskin", "saturation_sync"));

	public static final StreamCodec<RegistryFriendlyByteBuf, SaturationSyncPayload> CODEC =
			StreamCodec.composite(
					ByteBufCodecs.FLOAT,
					SaturationSyncPayload::saturation,
					SaturationSyncPayload::new
			);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
