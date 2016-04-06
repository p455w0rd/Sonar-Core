package sonar.core.handlers.energy;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import sonar.core.api.ActionType;
import sonar.core.api.EnergyHandler;
import sonar.core.api.EnergyType;
import sonar.core.api.StoredEnergyStack;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;

public class RFHandler extends EnergyHandler {

	public static String name = "RF-Provider";

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean canProvideEnergy(TileEntity tile, EnumFacing dir) {
		return tile != null && (tile instanceof IEnergyReceiver || tile instanceof IEnergyProvider);
	}

	@Override
	public void getEnergy(StoredEnergyStack energyStack, TileEntity tile, EnumFacing dir) {
		if (tile == null) {
			return;
		}
		if (tile instanceof IEnergyReceiver) {
			IEnergyReceiver receiver = (IEnergyReceiver) tile;
			energyStack.setStorageValues(receiver.getEnergyStored(dir), receiver.getMaxEnergyStored(dir));
			int simulateAdd = receiver.receiveEnergy(dir, Integer.MAX_VALUE, true);
			energyStack.setMaxInput(simulateAdd);
		}
		if (tile instanceof IEnergyProvider) {
			IEnergyProvider provider = (IEnergyProvider) tile;
			energyStack.setStorageValues(provider.getEnergyStored(dir), provider.getMaxEnergyStored(dir));
			int simulateRemove = provider.extractEnergy(dir, Integer.MAX_VALUE, true);
			energyStack.setMaxOutput(simulateRemove);
		}
	}

	@Override
	public long receiveEnergy(long maxReceive, TileEntity tile, EnumFacing dir, ActionType action) {
		long receive = maxReceive;
		if (tile instanceof IEnergyReceiver) {
			IEnergyReceiver receiver = (IEnergyReceiver) tile;
			if (receiver.canConnectEnergy(dir.getOpposite())) {
				int transferRF = maxReceive < Integer.MAX_VALUE ? (int) maxReceive : Integer.MAX_VALUE;
				receive -= receiver.receiveEnergy(dir.getOpposite(), transferRF, action.shouldSimulate());
			}
		}
		return receive;
	}

	@Override
	public long extractEnergy(long maxExtract, TileEntity tile, EnumFacing dir, ActionType action) {
		long extract = maxExtract;
		if (tile instanceof IEnergyProvider) {
			IEnergyProvider receiver = (IEnergyProvider) tile;
			if (receiver.canConnectEnergy(dir.getOpposite())) {
				int transferRF = maxExtract < Integer.MAX_VALUE ? (int) maxExtract : Integer.MAX_VALUE;
				extract -= receiver.extractEnergy(dir.getOpposite(), transferRF, action.shouldSimulate());
			}
		}
		return extract;
	}

	@Override
	public EnergyType getProvidedType() {
		return EnergyType.RF;
	}
}