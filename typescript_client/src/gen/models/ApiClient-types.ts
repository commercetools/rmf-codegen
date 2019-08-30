/* tslint:disable */
//Generated file, please do not change

import * as Joi from 'joi'


export function apiClientType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             id: Joi.string().required(),
             name: Joi.string().required(),
             scope: Joi.string().required(),
             lastUsedAt: Joi.date().iso().optional(),
             createdAt: Joi.date().iso().optional(),
             deleteAt: Joi.date().iso().optional(),
             secret: Joi.string().optional()
          })
}

export function apiClientDraftType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             name: Joi.string().required(),
             scope: Joi.string().required(),
             deleteDaysAfterCreation: Joi.number().optional()
          })
}

export function apiClientPagedQueryResponseType(): Joi.AnySchema {
   return Joi.object().unknown().keys({
             results: Joi.array().items(apiClientType()).required(),
             count: Joi.number().required(),
             offset: Joi.number().required(),
             total: Joi.number().optional()
          })
}