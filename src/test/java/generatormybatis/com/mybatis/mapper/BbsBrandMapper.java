package generatormybatis.com.mybatis.mapper;

import generatormybatis.com.mybatis.entity.BbsBrand;
import generatormybatis.com.mybatis.entity.BbsBrandExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BbsBrandMapper {
    int countByExample(BbsBrandExample example);

    int deleteByExample(BbsBrandExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BbsBrand record);

    int insertSelective(BbsBrand record);

    List<BbsBrand> selectByExample(BbsBrandExample example);

    BbsBrand selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BbsBrand record, @Param("example") BbsBrandExample example);

    int updateByExample(@Param("record") BbsBrand record, @Param("example") BbsBrandExample example);

    int updateByPrimaryKeySelective(BbsBrand record);

    int updateByPrimaryKey(BbsBrand record);
}