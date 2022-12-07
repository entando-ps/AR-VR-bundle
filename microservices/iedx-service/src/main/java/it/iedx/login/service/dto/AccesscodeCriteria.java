package it.iedx.login.service.dto;


import it.iedx.login.domain.AccessCode;
import it.iedx.login.web.rest.AccessCodeResource;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link AccessCode} entity. This class is used
 * in {@link AccessCodeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /accesscodes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AccesscodeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private ZonedDateTimeFilter generated;

    private IntegerFilter status;

    private ZonedDateTimeFilter used;

    private StringFilter qrcode;

    public AccesscodeCriteria() {
    }

    public AccesscodeCriteria(AccesscodeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.generated = other.generated == null ? null : other.generated.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.used = other.used == null ? null : other.used.copy();
        this.qrcode = other.qrcode == null ? null : other.qrcode.copy();
    }

    @Override
    public AccesscodeCriteria copy() {
        return new AccesscodeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCode() {
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public ZonedDateTimeFilter getGenerated() {
        return generated;
    }

    public void setGenerated(ZonedDateTimeFilter generated) {
        this.generated = generated;
    }

    public IntegerFilter getStatus() {
        return status;
    }

    public void setStatus(IntegerFilter status) {
        this.status = status;
    }

    public ZonedDateTimeFilter getUsed() {
        return used;
    }

    public void setUsed(ZonedDateTimeFilter used) {
        this.used = used;
    }

    public StringFilter getQrcode() {
        return qrcode;
    }

    public void setQrcode(StringFilter qrcode) {
        this.qrcode = qrcode;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AccesscodeCriteria that = (AccesscodeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(generated, that.generated) &&
            Objects.equals(status, that.status) &&
            Objects.equals(used, that.used) &&
            Objects.equals(qrcode, that.qrcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        code,
        generated,
        status,
        used,
        qrcode
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccesscodeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (code != null ? "code=" + code + ", " : "") +
                (generated != null ? "generated=" + generated + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (used != null ? "used=" + used + ", " : "") +
                (qrcode != null ? "qrcode=" + qrcode + ", " : "") +
            "}";
    }

}
