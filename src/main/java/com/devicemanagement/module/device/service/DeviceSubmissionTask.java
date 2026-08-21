package com.devicemanagement.module.device.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.devicemanagement.module.device.entity.*;
import com.devicemanagement.module.device.entity.pulldata.*;
import com.devicemanagement.module.device.mapper.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceSubmissionTask {

    private final DeviceMapper deviceMapper;
    private final DeviceAttrMapper deviceAttrMapper;
    private final DeviceAttrElectricMeterMapper electricMeterMapper;
    private final DeviceAttrAirStationMapper airStationMapper;
    private final DeviceAttrSurfaceWaterMapper surfaceWaterMapper;
    private final DeviceAttrTvocMapper tvocMapper;
    private final DeviceSubmissionElectricityMapper electricitySubmissionMapper;
    private final DeviceSubmissionAirStationMapper airStationSubmissionMapper;
    private final DeviceSubmissionSurfaceWaterMapper surfaceWaterSubmissionMapper;
    private final DeviceSubmissionTvocMapper tvocSubmissionMapper;

    /**
     * 用电设备定时推送：每30秒，只推在线设备
     */
    @Scheduled(fixedRate = 120000)
    @Async("deviceTaskExecutor")
    public void pushElectricityData() {
        log.info("开始推送用电监测数据...");
        List<DeviceAttr> attrs = deviceAttrMapper.selectList(
                new LambdaQueryWrapper<DeviceAttr>()
                        .eq(DeviceAttr::getAttrTag, "electric_meter"));
        int count = 0;
        for (DeviceAttr attr : attrs) {
            try {
                // 查设备是否在线
                Device device = deviceMapper.selectById(attr.getDeviceId());
                if (device == null || device.getStatus() == null || device.getStatus() != 1) {
                    continue;
                }

                DeviceAttrElectricMeter meter = electricMeterMapper.selectById(attr.getAttrId());
                if (meter == null) continue;

                DeviceSubmissionElectricity sub = new DeviceSubmissionElectricity();
                sub.setQn("QN-ELEC-" + System.currentTimeMillis());
                sub.setSt("ST-ELEC");
                sub.setCn("CN-ELEC-001");
                sub.setMn(String.valueOf(attr.getAttrId()));
                sub.setFlag("0");
                sub.setCp("DATA");
                sub.setDataTime(new Date());

                sub.setD7(meter.getCt_ratio() != null ? meter.getCt_ratio().toString() : "0");
                sub.setD8("1");
                sub.setD4127("0");
                sub.setD4137("0");
                sub.setD4147(meter.getQ1eq() != null ? meter.getQ1eq().toString() : "0");
                sub.setD4157(meter.getQ2eq() != null ? meter.getQ2eq().toString() : "0");
                sub.setD4167(meter.getQ3eq() != null ? meter.getQ3eq().toString() : "0");
                sub.setD4177(meter.getQ4eq() != null ? meter.getQ4eq().toString() : "0");
                sub.setD8193(meter.getUab() != null ? meter.getUab().toString() : "0");
                sub.setD8195(meter.getUbc() != null ? meter.getUbc().toString() : "0");
                sub.setD8197(meter.getUca() != null ? meter.getUca().toString() : "0");
                sub.setD8199(meter.getUa() != null ? meter.getUa().toString() : "0");
                sub.setD8201(meter.getUb() != null ? meter.getUb().toString() : "0");
                sub.setD8203(meter.getUc() != null ? meter.getUc().toString() : "0");
                sub.setD8205(meter.getIa() != null ? meter.getIa().toString() : "0");
                sub.setD8207(meter.getIb() != null ? meter.getIb().toString() : "0");
                sub.setD8209(meter.getIc() != null ? meter.getIc().toString() : "0");
                sub.setD8211(meter.getPt() != null ? meter.getPt().toString() : "0");
                sub.setD8213(meter.getPa() != null ? meter.getPa().toString() : "0");
                sub.setD8215(meter.getPb() != null ? meter.getPb().toString() : "0");
                sub.setD8217(meter.getPc() != null ? meter.getPc().toString() : "0");
                sub.setD8219(meter.getQt() != null ? meter.getQt().toString() : "0");
                sub.setD8221(meter.getQa() != null ? meter.getQa().toString() : "0");
                sub.setD8223(meter.getQb() != null ? meter.getQb().toString() : "0");
                sub.setD8225(meter.getQc() != null ? meter.getQc().toString() : "0");
                sub.setD8235(meter.getPft() != null ? meter.getPft().toString() : "0");
                sub.setD8237(meter.getPfa() != null ? meter.getPfa().toString() : "0");
                sub.setD8239(meter.getPfb() != null ? meter.getPfb().toString() : "0");
                sub.setD8241(meter.getPfc() != null ? meter.getPfc().toString() : "0");
                sub.setD8261(meter.getFreq() != null ? meter.getFreq().toString() : "0");

                electricitySubmissionMapper.insert(sub);
                count++;
            } catch (Exception e) {
                log.error("用电数据推送失败, attrId={}", attr.getAttrId(), e);
            }
        }
        log.info("用电监测数据推送完成, 在线设备{}条", count);
    }

    @Scheduled(fixedRate = 120000)
    @Async("deviceTaskExecutor")
    public void pushAirStationData() {
        log.info("开始推送空气微站数据...");
        List<DeviceAttr> attrs = deviceAttrMapper.selectList(
                new LambdaQueryWrapper<DeviceAttr>()
                        .eq(DeviceAttr::getAttrTag, "air_station"));
        int count = 0;
        for (DeviceAttr attr : attrs) {
            try {
                Device device = deviceMapper.selectById(attr.getDeviceId());
                if (device == null || device.getStatus() == null || device.getStatus() != 1) {
                    continue;
                }

                DeviceAttrAirStation station = airStationMapper.selectById(attr.getAttrId());
                if (station == null) continue;

                DeviceSubmissionAirStation sub = new DeviceSubmissionAirStation();
                sub.setQn("QN-AIR-" + System.currentTimeMillis());
                sub.setSt("ST-AIR");
                sub.setCn("CN-AIR-001");
                sub.setMn(String.valueOf(attr.getAttrId()));
                sub.setFlag("0");
                sub.setCp("DATA");
                sub.setDataTime(new Date());

                sub.setA21026Min(station.getSo2Min() != null ? station.getSo2Min().toString() : "0");
                sub.setA21026Avg(station.getSo2Avg() != null ? station.getSo2Avg().toString() : "0");
                sub.setA21026Max(station.getSo2Max() != null ? station.getSo2Max().toString() : "0");
                sub.setA21003Min(station.getNoMin() != null ? station.getNoMin().toString() : "0");
                sub.setA21003Avg(station.getNoAvg() != null ? station.getNoAvg().toString() : "0");
                sub.setA21003Max(station.getNoMax() != null ? station.getNoMax().toString() : "0");
                sub.setA21004Min(station.getNo2Min() != null ? station.getNo2Min().toString() : "0");
                sub.setA21004Avg(station.getNo2Avg() != null ? station.getNo2Avg().toString() : "0");
                sub.setA21004Max(station.getNo2Max() != null ? station.getNo2Max().toString() : "0");
                sub.setA21002Min(station.getNoxMin() != null ? station.getNoxMin().toString() : "0");
                sub.setA21002Avg(station.getNoxAvg() != null ? station.getNoxAvg().toString() : "0");
                sub.setA21002Max(station.getNoxMax() != null ? station.getNoxMax().toString() : "0");
                sub.setA21005Min(station.getCoMin() != null ? station.getCoMin().toString() : "0");
                sub.setA21005Avg(station.getCoAvg() != null ? station.getCoAvg().toString() : "0");
                sub.setA21005Max(station.getCoMax() != null ? station.getCoMax().toString() : "0");
                sub.setA34002Min(station.getPm10Min() != null ? station.getPm10Min().toString() : "0");
                sub.setA34002Avg(station.getPm10Avg() != null ? station.getPm10Avg().toString() : "0");
                sub.setA34002Max(station.getPm10Max() != null ? station.getPm10Max().toString() : "0");
                sub.setA34004Min(station.getPm25Min() != null ? station.getPm25Min().toString() : "0");
                sub.setA34004Avg(station.getPm25Avg() != null ? station.getPm25Avg().toString() : "0");
                sub.setA34004Max(station.getPm25Max() != null ? station.getPm25Max().toString() : "0");

                airStationSubmissionMapper.insert(sub);
                count++;
            } catch (Exception e) {
                log.error("空气微站数据推送失败, attrId={}", attr.getAttrId(), e);
            }
        }
        log.info("空气微站数据推送完成, 在线设备{}条", count);
    }

    @Scheduled(fixedRate = 120000)
    @Async("deviceTaskExecutor")
    public void pushSurfaceWaterData() {
        log.info("开始推送地表水数据...");
        List<DeviceAttr> attrs = deviceAttrMapper.selectList(
                new LambdaQueryWrapper<DeviceAttr>()
                        .eq(DeviceAttr::getAttrTag, "surface_water"));
        int count = 0;
        for (DeviceAttr attr : attrs) {
            try {
                Device device = deviceMapper.selectById(attr.getDeviceId());
                if (device == null || device.getStatus() == null || device.getStatus() != 1) {
                    continue;
                }

                DeviceAttrSurfaceWater water = surfaceWaterMapper.selectById(attr.getAttrId());
                if (water == null) continue;

                DeviceSubmissionSurfaceWater sub = new DeviceSubmissionSurfaceWater();
                sub.setQn("QN-WATER-" + System.currentTimeMillis());
                sub.setSt("ST-WATER");
                sub.setCn("CN-WATER-001");
                sub.setMn(String.valueOf(attr.getAttrId()));
                sub.setFlag("0");
                sub.setCp("DATA");
                sub.setDataTime(new Date());

                sub.setW01001(water.getPh_value() != null ? water.getPh_value().toString() : "0");
                sub.setW01003("0");
                sub.setW01009(water.getDissolved_oxygen() != null ? water.getDissolved_oxygen().toString() : "0");
                sub.setW01010(water.getWater_temperature() != null ? water.getWater_temperature().toString() : "0");
                sub.setW01014(water.getConductivity() != null ? water.getConductivity().toString() : "0");
                sub.setW01019(water.getPermanganate_index() != null ? water.getPermanganate_index().toString() : "0");
                sub.setW21001(water.getTotal_nitrogen() != null ? water.getTotal_nitrogen().toString() : "0");
                sub.setW21003(water.getAmmonia_nitrogen() != null ? water.getAmmonia_nitrogen().toString() : "0");
                sub.setW21011(water.getTotal_phosphorus() != null ? water.getTotal_phosphorus().toString() : "0");

                surfaceWaterSubmissionMapper.insert(sub);
                count++;
            } catch (Exception e) {
                log.error("地表水数据推送失败, attrId={}", attr.getAttrId(), e);
            }
        }
        log.info("地表水数据推送完成, 在线设备{}条", count);
    }

    /**
     * TVOC定时推送：每90秒，只推在线设备
     */
    @Scheduled(fixedRate = 120000)
    @Async("deviceTaskExecutor")
    public void pushTvocData() {
        log.info("开始推送TVOC数据...");
        List<DeviceAttr> attrs = deviceAttrMapper.selectList(
                new LambdaQueryWrapper<DeviceAttr>()
                        .eq(DeviceAttr::getAttrTag, "tvoc"));
        int count = 0;
        for (DeviceAttr attr : attrs) {
            try {
                Device device = deviceMapper.selectById(attr.getDeviceId());
                if (device == null || device.getStatus() == null || device.getStatus() != 1) {
                    continue;
                }

                DeviceAttrTvoc tvoc = tvocMapper.selectById(attr.getAttrId());
                if (tvoc == null) continue;

                DeviceSubmissionTvoc sub = new DeviceSubmissionTvoc();
                sub.setQn("QN-TVOC-" + System.currentTimeMillis());
                sub.setSt("ST-TVOC");
                sub.setCn("CN-TVOC-001");
                sub.setMn(String.valueOf(attr.getAttrId()));
                sub.setFlag("0");
                sub.setCp("DATA");
                sub.setDataTime(new Date());

                sub.setA01001(tvoc.getTemperature() != null ? tvoc.getTemperature().toString() : "0");
                sub.setA01002(tvoc.getHumidity() != null ? tvoc.getHumidity().toString() : "0");
                sub.setA01006(tvoc.getAir_pressure() != null ? tvoc.getAir_pressure().toString() : "0");
                sub.setA01007(tvoc.getWind_speed() != null ? tvoc.getWind_speed().toString() : "0");
                sub.setA01008(tvoc.getWind_direction() != null ? tvoc.getWind_direction() : "无");

                tvocSubmissionMapper.insert(sub);
                count++;
            } catch (Exception e) {
                log.error("TVOC数据推送失败, attrId={}", attr.getAttrId(), e);
            }
        }
        log.info("TVOC数据推送完成, 在线设备{}条", count);
    }
}