package com.zxq.iov.cloud.sp.vp.api.impl;

import com.zxq.iov.cloud.sp.vp.api.ITboxConfigService;
import com.zxq.iov.cloud.sp.vp.api.dto.config.KeyDto;
import com.zxq.iov.cloud.sp.vp.api.dto.OtaDto;
import com.zxq.iov.cloud.sp.vp.api.dto.config.TboxConfigDto;
import com.zxq.iov.cloud.sp.vp.api.dto.config.TboxConfigPackageDto;
import com.zxq.iov.cloud.sp.vp.api.dto.config.TboxConfigSettingDto;
import com.zxq.iov.cloud.sp.vp.common.BinaryAndHexUtil;
import com.zxq.iov.cloud.sp.vp.dao.config.ITboxDaoService;
import com.zxq.iov.cloud.sp.vp.dao.config.ITboxPersonalConfigDaoService;
import com.zxq.iov.cloud.sp.vp.entity.config.TboxPersonalConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 安防 远程配置服务实现类
 *
 * @author 叶荣杰
 * create date 2015-6-19 11:44
 * modify date 2015-7-24 13:18
 * @version 0.5, 2015-7-24
 */
@Service
@Qualifier("tboxConfigService")
public class TboxConfigServiceImpl extends BaseService implements ITboxConfigService {

    @Autowired
    private ITboxPersonalConfigDaoService tboxPersonalConfigDaoService;
    @Autowired
    private ITboxDaoService tboxDaoService;

    @Override
    public void requestConfigUpdate(String vin) throws Exception {
        AssertRequired("vin", vin);
        // 无业务操作
    }

    @Override
    public void responseConfigUpdate(OtaDto otaDto, Boolean isAccepted) {
        // 无业务操作，拒绝也没有原因？
    }

    @Override
    public TboxConfigDto checkConfigDelta(OtaDto otaDto, byte[] mcuVersion, byte[] mpuVersion, String vin,
                                          String iccid, byte[] configVersion, Integer configDelta) {
        TboxPersonalConfig tboxPersonalConfig = tboxPersonalConfigDaoService.findTboxPersonalConfigByTboxId(otaDto.getTboxId());
        TboxConfigDto tboxConfigDto = new TboxConfigDto();
        if(tboxPersonalConfig.getConfigDelta().intValue() > configDelta.intValue()) {
            tboxConfigDto.setPackageCount(1); // 这里packageCount怎么定？
            tboxConfigDto.setConfigDelta(tboxPersonalConfig.getConfigDelta());
        }
        return tboxConfigDto;
    }

    @Override
    public TboxConfigPackageDto getConfigPackage(OtaDto otaDto, Integer packageId) {
        TboxConfigPackageDto tboxConfigPackageDto = new TboxConfigPackageDto();
        // 这里package从哪获取，如何获取，怎么分解不详
        return tboxConfigPackageDto;
    }

    @Override
    public void closeConfigUpdate(OtaDto otaDto, Boolean result, byte[] mcuVersion, byte[] mpuVersion,
                                  byte[] configVersion, Integer configDelta) {
        // 暂时不知道需要做啥
    }

    @Override
    public void requestReadConfig(String vin, Long[] tboxConfigsettingIds) throws Exception {
        AssertRequired("vin", vin);
        // 无业务操作
    }

    @Override
    public void responseReadConfig(OtaDto otaDto, List<TboxConfigSettingDto> tboxConfigSettingDtos) {
        // 暂不知道做什么
    }

    @Override
    public KeyDto generateAsymmetricKey(OtaDto otaDto) {
        Long tboxId = null;
        if(null != otaDto.getTboxSn()) {
            // 根据TBOX SN定位到TBOX对象
        }
        else {
            // 根据TBOX ID定位到TBOX对象
        }
        // 生成非对称的public key, private key
        String publicKey = "";
        String privateKey = "";
        tboxDaoService.updateAsymmetricKey(tboxId, publicKey, privateKey); // 绑定tbox写入缓存
        KeyDto asymmetricKeyDto = new KeyDto();
        asymmetricKeyDto.setPublicKey(BinaryAndHexUtil.hexStringToByte(publicKey));
        return asymmetricKeyDto;
    }

    @Override
    public KeyDto bindTboxWithSecretKey(OtaDto otaDto, byte[] secretKeyWithEnc, byte[] tboxSnWithEnc) {
        Long tboxId = null;
        if(null != otaDto.getTboxSn()) {
            // 根据TBOX SN定位到TBOX对象
        }
        else {
            // 根据TBOX ID定位到TBOX对象
        }
        String privateKey = tboxDaoService.findPrivateKeyById(tboxId);  // 根据tbox从缓存中读出private key
        // 用private Key对tboxSnWithEnc进行解密，验证是否正确，如果正确的话对secretKey进行解密
        // 用private key对secretKey进行解密
        String secretKey = "";
        tboxDaoService.updateSecretKey(tboxId, secretKey);  // 绑定解密后的secretKey
        KeyDto keyDto = new KeyDto();
        keyDto.setEventId(otaDto.getEventId());
        keyDto.setTboxId(tboxId);
        keyDto.setSecretKeyAccepted(true);
        return keyDto;
    }
}
