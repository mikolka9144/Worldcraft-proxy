package com.mikolka9144.worldcraft.programs.simba;

import com.mikolka9144.worldcraft.socket.model.EventCodecs.BlockData;
import com.mikolka9144.worldcraft.socket.model.Vector3;
import com.mikolka9144.worldcraft.socket.model.Vector3Short;
import lombok.Setter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MonikasLovePen {

    @Setter
    private BlockData.BlockType playerPen = BlockData.BlockType.GLASS_ID;
    private Vector3 auditPoint = null;
    public List<BlockData> drawBlockLine(Vector3 beginPoint, Vector3 endPoint){
        return drawBlockLine(beginPoint,endPoint,auditPoint,new ArrayList<>());
    }

    private List<BlockData> drawBlockLine(Vector3 beginPoint, Vector3 endPoint,Vector3 auditPoint,List<BlockData> dropList){

        double stepSize = 0.25;
        double maximumLength = Math.max(Math.max(
                        Math.abs(endPoint.getX()- beginPoint.getX()),
                        Math.abs(endPoint.getZ()- beginPoint.getZ())
                ),
                Math.abs(endPoint.getY()- beginPoint.getY())
        );
        int stepsCount = (int) Math.floor(maximumLength/stepSize);

        float XShift = endPoint.getX()- beginPoint.getX();
        float YShift = endPoint.getY()- beginPoint.getY();
        float ZShift = endPoint.getZ()- beginPoint.getZ();
        for (int i = 0; i <= stepsCount; i++) {
            float precentage = (float) i /stepsCount;
            Vector3 newBlockPosition = new Vector3(
                    beginPoint.getX()+(XShift*precentage),
                    beginPoint.getY()+(YShift*precentage),
                    beginPoint.getZ()+(ZShift*precentage));
            Vector3Short pos = new Vector3Short(
                    (short) Math.floor(newBlockPosition.getX()),
                    (short) Math.floor(newBlockPosition.getY()),
                    (short) Math.floor(newBlockPosition.getZ())
            );

            if (dropList.stream().map(BlockData::getPosition).anyMatch(s -> s.equals(pos))) continue;
            dropList.add(new BlockData(pos, playerPen, (byte) 0));
            if(auditPoint != null) drawBlockLine(newBlockPosition,auditPoint,null,dropList);
        }
        return dropList;
    }
    public void setAuditPoint(Vector3 point){
        auditPoint = point;
    }
    public void resetAuditPoint(){
        setAuditPoint(null);
    }
}
