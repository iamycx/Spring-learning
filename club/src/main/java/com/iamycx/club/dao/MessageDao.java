package com.iamycx.club.dao;

import com.iamycx.club.model.Comment;
import com.iamycx.club.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by iamycx on 10:50 2017/12/27
 */
@Mapper
public interface MessageDao {
    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id, to_id, content, has_read, conversation_id, created_date ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{fromId},#{toId},#{content},#{hasRead},#{conversationId},#{createdDate})"})
    int addMessage(Message message);

    @Select({"select ", SELECT_FIELDS, " from", TABLE_NAME, " where conversation_id=#{conversationId} order by created_date desc limit #{offset}, #{limit}"})
    List<Message> getConversationByDetail(@Param("conversationId") String conversationId,@Param("offset") int offset,@Param("limit") int limit);

    @Select({"select ", INSERT_FIELDS, " , count(id) as id from ( select * from ", TABLE_NAME,
            " where from_id=#{userId} or to_id=#{userId} order by created_date desc) tt group by conversation_id order by created_date desc limit #{offset}, #{limit}"})
    List<Message> getConversationList(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);

    @Select({"select count(id) from ", TABLE_NAME, " where has_read=0 and to_id=#{userId} and conversation_id=#{conversationId}"})
    int getConversationUnreadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

//    @Select({"select count(id) from", TABLE_NAME, " where entity_id=#{entityId} and entity_type=#{entityType}"})
//    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

//    @Select({"select ",SELECT_FIELDS," from",TABLE_NAME," where name=#{name}"})
//    User selectByName(String name);
//
//    @Update({"update comment set status=#{status} where id=#{id}"})
//    int updateStatus(@Param("id") int id, @Param("status") int status);
//
//    @Delete({"delete from ",TABLE_NAME," where id=#{id}"})
//    void deleteById(int id);
}
